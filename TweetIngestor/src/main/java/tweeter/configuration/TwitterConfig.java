package tweeter.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;

@Configuration
public class TwitterConfig {

	@Bean
	public Twitter twitter(
			@Value("${spring.social.twitter.appId}") final String appId,
			@Value("${spring.social.twitter.appSecret}") final String appSecret,
			@Value("${spring.social.twitter.accessToken}") final String accessToken,
			@Value("${spring.social.twitter.accessTokenSecret}") final String accessTokenSecret) {
		return new TwitterTemplate(appId, appSecret, accessToken,
				accessTokenSecret);
	}
}
