package tweetprocessor;

import org.springframework.social.twitter.api.Tweet;

public class SlowMessageReceiver extends MessageReceiver {

	private static final int delay = 4000;

	@Override
	public void receiveMessage(Tweet message) {
		try {
			super.receiveMessage(message);
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
