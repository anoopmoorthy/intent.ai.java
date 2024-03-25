package com.bid.bservice.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.bid.bservice.entity.BidRequest;
import com.bid.bservice.entity.Exchange;

public interface BidService {
	public Exchange matchBidRequestToCampaign(BidRequest bidRequest);
	public Long addNewBidRequest(BidRequest bidRequest);
	public void currentBidStatus(SseEmitter emitter);
}
