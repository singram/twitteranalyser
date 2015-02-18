package tweetprocessor.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tweetprocessor.service.RedisRepository;

@Controller
@RequestMapping("/tags")
public class TagController {

	@Autowired
	private RedisRepository redisRepo;

    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody Set<Object> getTags() {
        return redisRepo.getTags();
    }

}
