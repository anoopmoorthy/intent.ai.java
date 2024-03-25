package com.bid.bservice.dao;

import com.bid.bservice.entity.MetricsExchange;

public interface MetricsDao {
	public int storeMetrics(MetricsExchange metrics);
}
