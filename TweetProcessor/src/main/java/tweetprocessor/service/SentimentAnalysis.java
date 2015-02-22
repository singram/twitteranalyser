package tweetprocessor.service;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

@Service
public class SentimentAnalysis {

	public static final StanfordCoreNLP NLP;

	static {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		NLP = new StanfordCoreNLP(props);
	}

	public synchronized int extract(String text) {
		int mainSentiment = 2;
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
}
