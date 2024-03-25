package com.bid.bservice.service;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bid.bservice.dao.MetricsDao;
import com.bid.bservice.entity.AdCampaign;
import com.bid.bservice.entity.BidRequest;
import com.bid.bservice.entity.MetricsExchange;

@Service
public class MetricsServiceImpl implements MetricsService{

	private MetricsDao metricsDao;
	
	@Autowired
	public MetricsServiceImpl(MetricsDao metricsDao) {
		super();
		this.metricsDao = metricsDao;
	}

	@Override
	public MetricsExchange calculateMetrics(BidRequest bidRequest, List<AdCampaign> campaigns, AdCampaign campaign ) {
		
		DoubleSummaryStatistics s = campaigns.stream().collect(Collectors.summarizingDouble(c -> c.getBudget()));
		
		MetricsExchange metrics = new MetricsExchange(s.getMax(), s.getMin(), 
				s.getCount(), s.getAverage(), 
				(campaign != null && campaign.getIab() != null) ? campaign.getIab()[0] : "");
		
		metricsDao.storeMetrics(metrics);
		
		return metrics;
	}

}
