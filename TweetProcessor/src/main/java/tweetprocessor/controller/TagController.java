package tweetprocessor.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tweetprocessor.service.RedisRepository;

@Controller
public class TagController {

	@SuppressWarnings("unused")
	private Logger log = Logger.getLogger(TagController.class);

	@Autowired
	private RedisRepository redisRepo;

	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public @ResponseBody
	void reset() {
		redisRepo.reset();
	}

	@RequestMapping(value = "/tags", method = RequestMethod.GET)
	public @ResponseBody
	Set<String> getTags() {
		return redisRepo.getTags();
	}

	@RequestMapping(value = "/tweet/volume", method = RequestMethod.GET)
	public @ResponseBody
	ArrayList<HashMap<String, String>> getTweetsAt() {
		ArrayList<HashMap<String, String>> returnSet = new ArrayList<HashMap<String, String>>();
		Map<String, Integer> data = redisRepo.getTweetsAtCount();
		for (Map.Entry<String, Integer> entry : data.entrySet()) {
			HashMap<String, String> record = new HashMap<String, String>();
			record.put("date", entry.getKey());
			// TODO(singram): What the hell is going on here such that .getClass doesn't
			// work but ""+entry does
			// log.info("VALUECLASS " + entry.getValue().getClass());
			record.put("count", "" + entry.getValue());
			returnSet.add(record);
		}
		return returnSet;
	}

	@RequestMapping(value = "/tweet/sentiment", method = RequestMethod.GET)
	public @ResponseBody
	ArrayList<HashMap<String, String>> getTweetSentimentAt() {
		ArrayList<HashMap<String, String>> returnSet = new ArrayList<HashMap<String, String>>();
		Map<String, Integer> data = redisRepo.getSentimentAtCount();
		String keys[];
		for (Map.Entry<String, Integer> entry : data.entrySet()) {
			HashMap<String, String> record = new HashMap<String, String>();
			keys = StringUtils.split(entry.getKey(),
					RedisRepository.FIELD_SEPERATOR);
			record.put("date", keys[0]);
			record.put("Sentiment", sentimentValueToString(keys[1]));
			// TODO(singram): What the hell is going on here such that .getClass doesn't
			// work but ""+entry does
			// log.info("VALUECLASS " + entry.getValue().getClass());
			record.put("count", "" + entry.getValue());
			returnSet.add(record);
		}
		return returnSet;
	}

	private static String sentimentValueToString(String val) {
		if (!StringUtils.isBlank(val)) {
			switch (val) {
			case "0":
				return "Very Negative";
			case "1":
				return "Negative";
			case "2":
				return "Neutral";
			case "3":
				return "Positive";
			case "4":
				return "Very Positive";
			default:
				return "Unknown";
			}
		} else {
			return "Unknown";
		}
	}

}
