package tweeter.service;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TweetTracker {

	private Logger log = Logger.getLogger(TweetTracker.class);

	private AtomicInteger tweets = new AtomicInteger(0);
	private AtomicInteger deletedEvents = new AtomicInteger(0);
	private AtomicInteger limitEvents = new AtomicInteger(0);
	private AtomicInteger warningEvents = new AtomicInteger(0);

	public void incrementTweets() {
		tweets.incrementAndGet();
	}

	public void incrementDeletedEvents() {
		deletedEvents.incrementAndGet();
	}

	public void incrementLimitEvents() {
		limitEvents.incrementAndGet();
	}

	public void incrementWarningEvents() {
		warningEvents.incrementAndGet();
	}

	@Override
	public String toString() {
		return (String.format("Tweets: %d Deleted: %d Limited: %d Warning %d",
				tweets.get(), deletedEvents.get(), limitEvents.get(),
				warningEvents.get()));
	}

	@Scheduled(fixedRate = 5000)
	public void reportStreamProgress() {
		log.info(this);
	}

}