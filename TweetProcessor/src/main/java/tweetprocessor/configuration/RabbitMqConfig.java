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

@Configuration
public class RabbitMqConfig {

	private static String queueName;

	@Bean
	Queue queue(final @Value("${spring.rabbitmq.tweeter.queue}") String queue) {
		// TODO(singram): Seems like there should be a far better way to handle this.
		RabbitMqConfig.queueName = queue;
		return new Queue(queueName, true);
	}

	@Bean
	TopicExchange exchange(final @Value("${spring.rabbitmq.tweeter.exchange}") String exchange) {
		return new TopicExchange(exchange);
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(queueName);
	}

	@Bean
	SimpleMessageListenerContainer container(
			ConnectionFactory connectionFactory,
			MessageListenerAdapter listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setConcurrentConsumers(10);
		container.setQueueNames(RabbitMqConfig.queueName);
		container.setMessageListener(listenerAdapter);
		return container;
	}

	@Bean
	MessageReceiver receiver() {
		 return new MessageReceiver();
		//return new SlowMessageReceiver();
	}

	@Bean
	MessageListenerAdapter listenerAdapter(MessageReceiver receiver) {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

}
