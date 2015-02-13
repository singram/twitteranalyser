package tweeter;

import java.util.Date;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.time.FastDateFormat;

@Component
public class ScheduledTasks {

	private static final FastDateFormat dateFormat = FastDateFormat.getInstance("HH:mm:ss");

	@Scheduled(fixedRate = 5000)
	public void reportCurrentTime() {
		System.out.println("The time is now " + dateFormat.format(new Date()));
	}

}