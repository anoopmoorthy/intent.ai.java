package com.bid.bservice.controller;

import java.time.LocalDate;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.bid.bservice.entity.BidRequest;
import com.bid.bservice.entity.Exchange;
import com.bid.bservice.service.BidService;
import com.bid.bservice.service.RedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/bid")
@Slf4j
//https://github.com/intent-ai/engineering-interview
public class BidController {
	
	private BidService bidService;
	
	@Autowired
	public BidController(BidService bidService) {
		super();
		this.bidService = bidService;
	}

	@RequestMapping(method = RequestMethod.POST, path = "/request")
	public void getData(@RequestBody BidRequest bidRequest) throws JsonProcessingException, InterruptedException {
		bidService.addNewBidRequest(bidRequest);
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/response")
    public SseEmitter repsonse() {
        SseEmitter emitter = new SseEmitter();
        bidService.currentBidStatus(emitter);
        return emitter;
    }
}
