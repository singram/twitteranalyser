package tweeter.service;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Service;

import tweeter.configuration.RabbitMqConfig;

@Service
@Async("tweetTaskExecutor")
@Configuration
public class TweetProcessorToRabbit implements TweetProcessor {

	private Logger log = Logger.getLogger(TweetProcessorToRabbit.class);

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Override
	public void processTweet(Tweet tweet) {
		log.info("Processing " + tweet.getText());
		if (tweet.getLanguageCode().equalsIgnoreCase("en")) {
			log.info(tweet.getExtraData().toString());
			rabbitTemplate.convertAndSend(RabbitMqConfig.queueName, tweet);
		}
	}

}