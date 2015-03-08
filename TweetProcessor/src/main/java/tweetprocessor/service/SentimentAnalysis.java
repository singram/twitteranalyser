package tweetprocessor.service;

import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import tweetprocessor.MessageReceiver;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

@Service
public class SentimentAnalysis {

	private Logger log = Logger.getLogger(SentimentAnalysis.class);

	public static final StanfordCoreNLP NLP;

	static {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		NLP = new StanfordCoreNLP(props);
	}

	/**
	 * Analyze the text provided and return a score based upon the sentiment of the text.
	 * @param text  The text provide a sentiment score on 
	 * @return      Number between -2 & 2 where -2 is very negative and +2 is very positive
	 */
	public synchronized int extract(String text) {
		int mainSentiment = 2;
		text = cleanText(text);
		log.debug("Sentiment text: " + text);
		if (!StringUtils.isBlank(text)) {
			int longest = 0;
			Annotation annotation = NLP.process(text);
			// Longest sentence governs sentiment returned.
			for (CoreMap sentence : annotation
					.get(CoreAnnotations.SentencesAnnotation.class)) {
				Tree tree = sentence
						.get(SentimentCoreAnnotations.AnnotatedTree.class);
				int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
				String partText = sentence.toString();
				if (partText.length() > longest) {
					mainSentiment = sentiment;
					longest = partText.length();
				}

			}
		}
		return mainSentiment - 2;
	}
	
	private static final String HASHTAG_PATTERN = "#\\w+";
	private static final String LINK_PATTERN = "https?://[\\w\\./]+";
	public String cleanText(String text) {
		// Remove non-ascii content (usually emoticons etc)
		String cleanText = text.replaceAll("[^\\x00-\\x7F]", "");
		// Strip hash tags
		cleanText = cleanText.replaceAll(HASHTAG_PATTERN, "");
		// Strip links
		cleanText = cleanText.replaceAll(LINK_PATTERN, "");
		return cleanText;
	}
}
