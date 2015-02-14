package tweeter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.social.NotAuthorizedException;
import org.springframework.social.twitter.api.*;
import org.springframework.stereotype.Service;

import org.apache.log4j.Logger;

import java.util.*;

@Service
public class TwitterStreamingIngester implements StreamListener {

	// https://dev.twitter.com/streaming/overview/messages-types
	
	@Autowired
	private Twitter twitter;
	
	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;
	
	@Autowired
	private TweetProcessor tweetProcessor;

	private Logger log = Logger.getLogger(TwitterStreamingIngester.class);

	public void run() {
		if (isAuthorized()) {
			List<StreamListener> listeners = new ArrayList<StreamListener>();
			listeners.add(this);
			twitter.streamingOperations().sample(listeners);
			log.info("Listening to Twitter stream");
		}
	}

	private boolean isAuthorized() {
		// TODO There should be a better way of doing this.
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
		tweetProcessor.processTweet(tweet);
	}

	@Override
	public void onDelete(StreamDeleteEvent deleteEvent) {
		log.debug("Delete event: " + deleteEvent.getTweetId());
	}

	@Override
	public void onLimit(int numberOfLimitedTweets) {
		log.debug("Limit event: " + numberOfLimitedTweets);
	}

	@Override
	public void onWarning(StreamWarningEvent warningEvent) {
		log.warn("Warning event: " + warningEvent.getMessage());
	}
}