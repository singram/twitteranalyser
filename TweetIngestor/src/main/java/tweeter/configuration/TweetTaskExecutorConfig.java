package tweeter.configuration;

//import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
//public class TaskExecutorConfig {
//	@Bean
//	public ThreadPoolTaskExecutor taskExecutor(
//			final @Value("${taskExecutor.corePoolSize}") int corePoolSize,
//			final @Value("${taskExecutor.maxPoolSize}") int maxPoolSize,
//			final @Value("${taskExecutor.queueCapacity}") int queueCapacity) {
//		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//		taskExecutor.setCorePoolSize(corePoolSize);
//		taskExecutor.setMaxPoolSize(maxPoolSize);
//		taskExecutor.setQueueCapacity(queueCapacity);
//		return taskExecutor;
//	}
//}

@Configuration
@ConfigurationProperties(prefix = "taskExecutor")
public class TweetTaskExecutorConfig {
	private int corePoolSize;
	private int maxPoolSize;
	private int queueCapacity;

	public int getCorePoolSize() {
		return corePoolSize;
	}

	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public int getQueueCapacity() {
		return queueCapacity;
	}

	public void setQueueCapacity(int queueCapacity) {
		this.queueCapacity = queueCapacity;
	}

	@Bean(name = "tweetTaskExecutor")
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(corePoolSize);
		taskExecutor.setMaxPoolSize(maxPoolSize);
		taskExecutor.setQueueCapacity(queueCapacity);
		return taskExecutor;
	}
}