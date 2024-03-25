package com.bid.bservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.bid.bservice.entity.BidRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RedisService {

    private static final String QUEUE_KEY = "bid";
    
    private RedisTemplate<String, BidRequest> redisTemplate;
    
    @Autowired
    public RedisService(RedisTemplate<String, BidRequest> redisTemplate) {
		super();
		this.redisTemplate = redisTemplate;
		log.info("Redis Connection :{}", redisTemplate.getClientList());
	}

	public Long enqueueJsonData(BidRequest bidRequest) {
        return redisTemplate.opsForList().rightPush(QUEUE_KEY, bidRequest);
    }

    public BidRequest dequeueJsonData() {
        return redisTemplate.opsForList().leftPop(QUEUE_KEY);
    }
    
    public boolean hasData() {
    	return redisTemplate.opsForList().size(QUEUE_KEY) > 0;
    }
}
