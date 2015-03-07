package tweetprocessor;

import java.io.Serializable;
import java.util.HashMap;

public class SlowMessageReceiver extends MessageReceiver {

	private static final int delay = 4000;

	@Override
	public void receiveMessage(HashMap<String, Serializable> map) {
		try {
			super.receiveMessage(map);
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
