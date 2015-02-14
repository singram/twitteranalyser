package tweeter;

import java.util.Arrays;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import tweeter.service.TweetProcessorToRabbit;
import tweeter.service.TwitterStreamingIngester;

@Configuration
@ComponentScan(basePackages = "tweeter")
@EnableAutoConfiguration
@EnableScheduling
@EnableAsync
public class Application implements CommandLineRunner
{

	@Autowired
	private TwitterStreamingIngester twitterStreamingIngester;

	// TODO Pull beans out into separate configuration class for rabbit :-)  Bill says so.
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
	
	public static void main(String[] args) throws Exception {
		
		ApplicationContext ctx = SpringApplication.run(Application.class);

//		System.out.println("Let's inspect the beans provided by Spring Boot:");
//
//		String[] beanNames = ctx.getBeanDefinitionNames();
//		Arrays.sort(beanNames);
//		for (String beanName : beanNames) {
//			System.out.println(beanName);
//		}
	}

	@Override
	public void run(String... args) throws Exception {
		twitterStreamingIngester.run();
	}
}
