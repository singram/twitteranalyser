package tweeter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.social.NotAuthorizedException;
import org.springframework.social.twitter.api.*;
import org.springframework.stereotype.Service;

import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Service
public class TwitterStreamingIngester implements StreamListener {

	// https://dev.twitter.com/streaming/overview/messages-types
	
	@Autowired
	private Twitter twitter;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	private BlockingQueue<Tweet> queue = new ArrayBlockingQueue<Tweet>(20);
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

	@PostConstruct
	public void afterPropertiesSet() {
		setThreads();
	}

	public void setThreads() {
		for (int i = 0; i < taskExecutor.getMaxPoolSize(); i++) {
			taskExecutor.execute(new TweetProcessor(queue));
		}
	}

	@Override
	public void onTweet(Tweet tweet) {
		if (!queue.offer(tweet)) {
			log.warn("Queue capacity reached.  Tweet dropped");
		}
	}

	@Override
	public void onDelete(StreamDeleteEvent deleteEvent) {
		log.info("Delete event");
	}

	@Override
	public void onLimit(int numberOfLimitedTweets) {
		log.info("Limit event");
	}

	@Override
	public void onWarning(StreamWarningEvent warningEvent) {
		log.info("Warning event");
	}
}