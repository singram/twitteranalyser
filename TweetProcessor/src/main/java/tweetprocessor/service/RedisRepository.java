package tweetprocessor.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisRepository {

	@Autowired
	private StringRedisTemplate template;

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


}
