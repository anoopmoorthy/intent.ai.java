package com.bid.bservice.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.bid.bservice.controller.BidController;
import com.bid.bservice.dao.BidDao;
import com.bid.bservice.entity.AdCampaign;
import com.bid.bservice.entity.BidRequest;
import com.bid.bservice.entity.Exchange;

import com.bid.bservice.entity.MetricsExchange;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BidServiceImpl implements BidService{

	private MetricsService metricsService;
	private BidDao bidDao;
	private RedisService redisService;
	private ExecutorService executor;
	private ObjectMapper objectMapper;
	
	@Autowired
	public BidServiceImpl(ObjectMapper objectMapper,
			BidDao bidDao, 
			MetricsService metricsService,
			RedisService redisService,
			ExecutorService executor) {
		super();
		this.bidDao = bidDao;
		this.metricsService = metricsService;
		this.redisService = redisService;
		this.executor = executor;
		this.objectMapper = objectMapper;
	}
	
	@Override
	public Exchange matchBidRequestToCampaign(BidRequest bidRequest) {
		
		List<AdCampaign> adCampaign = bidDao.generateAdCampaigns();
		
		AdCampaign campaign = adCampaign
		.stream()
		.filter(c -> !bidRequest.getBcat().contains(c.getIab()))
		.filter(c -> bidRequest.getDevice().getDevicetype().equals(c.getTargeting().getDeviceType().getDevicetype()))
		.filter(c -> {
				Integer h = c.getImp().getBanner().getH();
				Integer w = c.getImp().getBanner().getW();
				
				return bidRequest
					.getImp()
					.stream()
					
					.anyMatch(b -> b.getBanner().getH().equals(h) && 
									b.getBanner().getW().equals(w) &&
									b.getExt().getIntent().getPlacementId().equals(c.getImp().getExt().getIntent().getPlacementId())
								);
			})
			.collect(Collectors.maxBy(Comparator.comparing(c -> c.getBudget())))
			.orElse(null);
		
		MetricsExchange stats = metricsService.calculateMetrics(bidRequest, adCampaign, campaign);
		
		Exchange exchange = new Exchange(bidRequest, campaign, adCampaign, stats);

		return exchange;
	}

	@Override
	public Long addNewBidRequest(BidRequest bidRequest) {
		return redisService.enqueueJsonData(bidRequest);
	}

	@Override
	public void currentBidStatus(SseEmitter emitter) {
        emitter.onCompletion(() -> log.info("connection complete :{}", LocalDate.now()));
        emitter.onTimeout(() -> emitter.complete());
        emitter.onError((e) -> {
        	emitter.completeWithError(e);
        });

        executor.execute(() -> {
            try {
            	while(redisService.hasData()) {
            		Thread.sleep(1000);
            		BidRequest bidRequest = redisService.dequeueJsonData();
            		Exchange exchange = matchBidRequestToCampaign(bidRequest);
            		String json = objectMapper.writeValueAsString(exchange);
            		
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .id(String.valueOf(0))
                            .name("message")
                            .data(json);
                    emitter.send(event);
            	}
                emitter.complete();
            } catch (Exception e) {
            	log.error("error :{}", e.getMessage());
                emitter.completeWithError(e);
            }
        });
	}
}
