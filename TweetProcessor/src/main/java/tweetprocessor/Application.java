package tweetprocessor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
//@ComponentScan(basePackages = "tweetprocessor")
@ComponentScan
@EnableAutoConfiguration
@EnableScheduling
public class Application {

	@Autowired
	RabbitTemplate rabbitTemplate;

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
//		ApplicationContext ctx = SpringApplication.run(Application.class);

		// System.out.println("Let's inspect the beans provided by Spring Boot:");
		//
		// String[] beanNames = ctx.getBeanDefinitionNames();
		// Arrays.sort(beanNames);
		// for (String beanName : beanNames) {
		// System.out.println(beanName);
		// }
	}

}
