package tweeter.service;

import org.springframework.social.twitter.api.Tweet;

public interface TweetProcessor {

	void process(Tweet tweet);
}
