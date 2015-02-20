package tweetprocessor;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.social.twitter.api.Tweet;

import tweetprocessor.service.RedisRepository;
import tweetprocessor.service.SentimentAnalysis;

public class MessageReceiver {

	private Logger log = Logger.getLogger(MessageReceiver.class);

	@Autowired
	private RedisRepository redisRepo;

	@Autowired
	private SentimentAnalysis sentiment;

	private AtomicInteger tweetCounter = new AtomicInteger(0);

	private CountDownLatch latch = new CountDownLatch(1);

	@Scheduled(fixedRate = 5000)
	public void reportCurrentProgress() {
		log.info(String.format("Processed %d messages", tweetCounter.get()));
	}

	public MessageReceiver() {
		log.info("Creating MessageReceiver");
	}

	public void receiveMessage(Tweet message) {
		Set<String> hashTags = hashtagsFromTweet(message.getText());
		if (!hashTags.isEmpty()) {
			redisRepo.incrementTagCounts(hashTags);
			log.info(hashTags.toString());
		}
		int sentiment = this.sentiment.extract(message.getText());
		log.debug(sentiment + " || " + message.getText());
		redisRepo.incrementTweetsAtCount(message.getCreatedAt());
		redisRepo.incrementSentimentAtCount(message.getCreatedAt(), sentiment);
		tweetCounter.incrementAndGet();
		latch.countDown();
	}

	public CountDownLatch getLatch() {
		return latch;
	}

	private static final Pattern HASHTAG_PATTERN = Pattern.compile("#\\w+");

	private static Set<String> hashtagsFromTweet(String text) {
		Set<String> hashtags = new HashSet<String>();
		Matcher matcher = HASHTAG_PATTERN.matcher(text);
		while (matcher.find()) {
			String tag = matcher.group();
			// removing '#' prefix
			hashtags.add(tag.substring(1));
		}
		return hashtags;
	}

}
