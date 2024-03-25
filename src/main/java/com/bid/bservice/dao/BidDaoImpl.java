package com.bid.bservice.dao;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Repository;

import com.bid.bservice.db.DataSource;
import com.bid.bservice.entity.AdCampaign;

@Repository
public class BidDaoImpl implements BidDao{
	
	public static Random random = new Random();

	@Override
	public AdCampaign generateAdCampaign() {
		
		AdCampaign adCampaign = new AdCampaign();
		
		adCampaign.setImp(DataSource.imp());
		adCampaign.setName(DataSource.campaignName());
		adCampaign.setId(DataSource.id());
		adCampaign.setTargeting(DataSource.targeting());
		adCampaign.setIab(new String[] {DataSource.iab()});
		adCampaign.setPlacementIds(DataSource.placementIds());
		adCampaign.setBudget(DataSource.budget());

		return adCampaign;
	}

	@Override
	public List<AdCampaign> generateAdCampaigns() {
		
		int num = random.nextInt(50) + 1;
		
		while( !(num > 0) ) {
			num = random.nextInt(50) + 1;
		}
		
		List<AdCampaign> adCampaigns = IntStream
				.range(1, random.nextInt(50) + 1)
				.boxed()
				.map(n -> generateAdCampaign())
				.collect(Collectors.toList());
				
			return adCampaigns;
	}
}
