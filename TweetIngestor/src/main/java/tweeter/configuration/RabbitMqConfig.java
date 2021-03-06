package tweeter.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

	public static String queueName;

	@Bean
	Queue queue(@Value("${spring.rabbitmq.tweeter.queue}") final String queue) {
		RabbitMqConfig.queueName = queue;
		return new Queue(queueName, true);
	}

	@Bean
	TopicExchange exchange(@Value("${spring.rabbitmq.tweeter.exchange}") final String exchange) {
		return new TopicExchange(exchange);
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(queueName);
	}

}
