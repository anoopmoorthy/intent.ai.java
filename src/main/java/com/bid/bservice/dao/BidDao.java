package com.bid.bservice.dao;

import java.util.List;

import com.bid.bservice.entity.AdCampaign;

public interface BidDao {
	public AdCampaign generateAdCampaign();
	public List<AdCampaign> generateAdCampaigns();
}
