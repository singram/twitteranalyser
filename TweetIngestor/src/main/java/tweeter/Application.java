package tweeter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import tweeter.service.TwitterStreamingIngester;

@Configuration
@ComponentScan(basePackages = "tweeter")
@EnableAutoConfiguration
@EnableScheduling
@EnableAsync
public class Application implements CommandLineRunner {

	@Autowired
	private TwitterStreamingIngester twitterStreamingIngester;

	public static void main(String[] args) throws Exception {

		SpringApplication.run(Application.class);
		// ApplicationContext ctx = SpringApplication.run(Application.class);

		// System.out.println("Let's inspect the beans provided by Spring Boot:");
		//
		// String[] beanNames = ctx.getBeanDefinitionNames();
		// Arrays.sort(beanNames);
		// for (String beanName : beanNames) {
		// System.out.println(beanName);
		// }
	}

	@Override
	public void run(String... args) throws Exception {
		twitterStreamingIngester.run();
	}
}
