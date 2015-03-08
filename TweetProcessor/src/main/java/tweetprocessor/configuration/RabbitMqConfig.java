package tweetprocessor.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tweetprocessor.MessageReceiver;
import tweetprocessor.SlowMessageReceiver;

@Configuration
public class RabbitMqConfig {

	@Bean
	Queue queue(@Value("${spring.rabbitmq.tweeter.queue}") final String queueName) {
		return new Queue(queueName, true);
	}

	@Bean
	TopicExchange exchange(@Value("${spring.rabbitmq.tweeter.exchange}") final String exchange) {
		return new TopicExchange(exchange);
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange, @Value("${spring.rabbitmq.tweeter.queue}") final String queueName) {
		return BindingBuilder.bind(queue).to(exchange).with(queueName);
	}

	@Bean
	SimpleMessageListenerContainer container(
			ConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter,
			@Value("${spring.rabbitmq.tweeter.queue}") final String queueName) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setConcurrentConsumers(10);
		container.setQueueNames(queueName);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	// TODO(singram): Perhaps there is a better way to do this with application.properties
	@Bean
	MessageReceiver receiver() {
		// return new MessageReceiver();
		return new SlowMessageReceiver();
	}

	@Bean
	MessageListenerAdapter listenerAdapter(MessageReceiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

}
