package tweetprocessor;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.social.twitter.api.Tweet;

public class MessageReceiver {

	@Autowired
	private StringRedisTemplate template;

	private Logger log = Logger.getLogger(MessageReceiver.class);

	private CountDownLatch latch = new CountDownLatch(1);

	@Scheduled(fixedRate = 5000)
	public void reportCurrentProgress() {
		log.info(String.format("Processed %d messages", tweetCounter.get()));
	}

	public MessageReceiver() {
		log.info("Creating MessageReceiver");
	}

	public void incrementTagCounts(Set<String> hashTags) {
		for (String tag : hashTags) {
			incrementTagCount(tag);
		}
	}

	public void incrementTagCount(String tag) {
		HashOperations<String, String, Integer> ops = this.template
				.opsForHash();
		String hashId = "tagcounts";
		ops.increment(hashId, tag, 1);
	}

	private AtomicInteger tweetCounter = new AtomicInteger(0);

	public void receiveMessage(Tweet message) {
		Set<String> hashTags = hashtagsFromTweet(message.getText());
		if (!hashTags.isEmpty()) {
			incrementTagCounts(hashTags);
			log.info(hashTags.toString());
		}
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
