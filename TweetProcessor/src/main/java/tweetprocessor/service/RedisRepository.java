package tweetprocessor.service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisRepository {

	@Autowired
	private StringRedisTemplate template;

	// Aggregation duration in seconds
	public static final int REPORTING_PERIOD = 5;
	public static final String FIELD_SEPERATOR = "|";
	private static final String TAG_COUNTS = "tagcounts";
	private static final String TWEET_COUNT = "tweet_count";
	private static final String TWEETS_AT_COUNT = "tweets_at_count";
	private static final String SENTIMENT_COUNT = "sentimentcount";
	private static final String SENTIMENT_AT_COUNT = "sentimentatcount";

	private static final List<String> HASH_KEYS = Arrays.asList(TAG_COUNTS,
			TWEET_COUNT, TWEETS_AT_COUNT, SENTIMENT_COUNT, SENTIMENT_AT_COUNT);

	public void reset() {
		this.template.delete(HASH_KEYS);
	}

	public void incrementTagCounts(Set<String> hashTags) {
		for (String tag : hashTags) {
			incrementTagCount(tag);
		}
	}

	public void incrementTagCount(String tag) {
		hashOps().increment(TAG_COUNTS, tag, 1);
	}

	public void incrementTweetsAtCount(Date date) {
		incrementTweetCount();
		hashOps().increment(TWEETS_AT_COUNT, formatDate(date), 1);
	}

	private void incrementTweetCount() {
		hashOps().increment(TWEET_COUNT, "total_count", 1);
	}

	public void incrementSentimentAtCount(Date date, int sentiment) {
		// sentiment range is -2 -> +2
		incrementSentimentCount(sentiment);
		hashOps().increment(SENTIMENT_AT_COUNT,
				formatDate(date) + FIELD_SEPERATOR + (sentiment + 2), 1);
	}

	private void incrementSentimentCount(int sentiment) {
		hashOps().increment(SENTIMENT_COUNT, ((Integer) sentiment).toString(),
				1);
	}

	public Set<String> getTags() {
		return hashOps().keys(TAG_COUNTS);
	}

	public Map<String, Integer> getTagCounts() {
		return hashOps().entries(TAG_COUNTS);
	}

	public Map<String, Integer> getTweetsAtCount() {
		return hashOps().entries(TWEETS_AT_COUNT);
	}

	public Map<String, Integer> getSentimentAtCount() {
		return hashOps().entries(SENTIMENT_AT_COUNT);
	}

	private static final String DATEFORMAT = "yyyy/MM/dd_HH:mm:ss";

	private static String formatDate(Date date) {
		return FastDateFormat.getInstance(DATEFORMAT).format(roundDate(date));
	}

	@SuppressWarnings("deprecation")
	private static Date roundDate(Date date) {
		date = DateUtils.truncate(date, Calendar.SECOND);
		return DateUtils.setSeconds(date,
				date.getSeconds() - (date.getSeconds() % REPORTING_PERIOD));
	}

	private HashOperations<String, String, Integer> hashOps() {
		return this.template.opsForHash();
	}

}
