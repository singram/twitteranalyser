package tweeter.service;

import org.springframework.social.twitter.api.Tweet;

import java.util.concurrent.BlockingQueue;

public class TweetProcessor implements Runnable {
	private final BlockingQueue<Tweet> queue;

	public TweetProcessor(BlockingQueue<Tweet> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Tweet tweet = queue.take();
				processTweet(tweet);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void processTweet(Tweet tweet) {
		if (tweet.getLanguageCode().equalsIgnoreCase("en")) {
			String text = tweet.getText();
			System.out.println("TWEET: " + text);
		}
	}

}