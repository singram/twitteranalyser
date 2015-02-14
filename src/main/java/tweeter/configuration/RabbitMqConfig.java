package tweeter.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;

import tweeter.service.TweetProcessorToRabbit;

public class RabbitMqConfig {

	@Bean
	Queue queue() {
		return new Queue(TweetProcessorToRabbit.queueName, false);
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange("spring-boot-exchange");
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(TweetProcessorToRabbit.queueName);
	}


}
