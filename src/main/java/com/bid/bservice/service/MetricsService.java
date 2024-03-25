package com.bid.bservice.service;

import java.util.List;

import com.bid.bservice.entity.AdCampaign;
import com.bid.bservice.entity.BidRequest;
import com.bid.bservice.entity.MetricsExchange;

public interface MetricsService {
	public MetricsExchange calculateMetrics(BidRequest bidRequest, List<AdCampaign> campaigns, AdCampaign campaign);
}
