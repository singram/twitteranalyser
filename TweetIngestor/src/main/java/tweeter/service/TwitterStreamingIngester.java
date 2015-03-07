package tweeter.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.social.NotAuthorizedException;
import org.springframework.social.twitter.api.FilterStreamParameters;
import org.springframework.social.twitter.api.StreamDeleteEvent;
import org.springframework.social.twitter.api.StreamListener;
import org.springframework.social.twitter.api.StreamWarningEvent;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;

@Service
public class TwitterStreamingIngester implements StreamListener {

	// https://dev.twitter.com/streaming/overview/messages-types

	@Autowired
	private TweetTracker tweetTracker;

	@Autowired
	private Twitter twitter;

	@Autowired
	private ThreadPoolTaskExecutor tweetTaskExecutor;

	@Autowired
	private TweetProcessor tweetProcessor;

	private Logger log = Logger.getLogger(TwitterStreamingIngester.class);

	public void run() {
		if (isAuthorized()) {
			List<StreamListener> listeners = new ArrayList<StreamListener>();
			listeners.add(this);
			FilterStreamParameters parameters = new FilterStreamParameters();
			// PA Coordinates as per
			// http://www.netstate.com/states/geography/pa_geography.htm
			parameters.addLocation(-80.31f, 39.43f, -74.43f, 42.0f);
			twitter.streamingOperations().filter(parameters, listeners);
			//twitter.streamingOperations().sample(listeners);
			log.info("Listening to Twitter stream - " + listeners.size()
					+ "listener(s)");
		}
	}

	private boolean isAuthorized() {
		// TODO(ingrams): There should be a better way of doing this.
		boolean authorized = false;
		try {
			String userName = twitter.userOperations().getAccountSettings()
					.getScreenName();
			authorized = true;
			log.info("Twitter authentication success for user - " + userName);
		} catch (NotAuthorizedException e) {
			log.error("Twitter authentication failed: " + e.getMessage());
		}
		return authorized;
	}

	@Override
	public void onTweet(Tweet tweet) {
		tweetTracker.incrementTweetCounter();
		tweetProcessor.process(tweet);
	}

	@Override
	public void onDelete(StreamDeleteEvent deleteEvent) {
		tweetTracker.incrementDeletedEvents();
		log.debug("Delete event: " + deleteEvent.getTweetId());
	}

	@Override
	public void onLimit(int numberOfLimitedTweets) {
		tweetTracker.incrementLimitEvents();
		log.debug("Limit event: " + numberOfLimitedTweets);
	}

	@Override
	public void onWarning(StreamWarningEvent warningEvent) {
		tweetTracker.incrementWarningEvents();
		log.warn("Warning event: " + warningEvent.getMessage());
	}
}
