package tweeter.service;

import java.io.Serializable;
import java.util.HashMap;

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

	@Autowired
	private TweetTracker tweetTracker;

	@Override
	public void process(Tweet tweet) {
		log.debug("Processing: " + tweet.getText());
		if (tweet.getLanguageCode().equalsIgnoreCase("en")) {
			HashMap<String, Serializable> map = new HashMap<String, Serializable>();
			map.put("tweet", tweet);
			map.put("user", tweet.getUser());	
			rabbitTemplate.convertAndSend(RabbitMqConfig.queueName, map);
			tweetTracker.incrementTweetsToSinkCounter();
		}
	}

}
