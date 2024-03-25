package com.bid.bservice.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Exchange {
	BidRequest bidRequest;
	AdCampaign winningCampaign;
	List<AdCampaign> campaigns;
	MetricsExchange statistic;
}
