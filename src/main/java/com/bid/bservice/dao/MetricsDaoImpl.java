package com.bid.bservice.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bid.bservice.entity.MetricsExchange;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class MetricsDaoImpl implements MetricsDao{
	
	NamedParameterJdbcTemplate npjTemplate;

	@Autowired
	public MetricsDaoImpl(@Qualifier("DefaultH2NamedParameterJdbcTemplate") NamedParameterJdbcTemplate npjTemplate) {
		super();
		this.npjTemplate = npjTemplate;
	}

	@Override
	public int storeMetrics(MetricsExchange metrics) {
		
		String sql = "INSERT INTO exchange_metrics(min_bid_price, max_bid_price, avg_bid_price, response_rate, iab) "
				+ " VALUES(:maximum, :minimum, :average, :totalling, :iab)";
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("average", metrics.getAverage());
		parameters.addValue("maximum", metrics.getMax());
		parameters.addValue("minimum", metrics.getMin());
		parameters.addValue("totalling", metrics.getCount());
		parameters.addValue("iab", metrics.getIab());
		int status = 0;
		
		try {
			status = npjTemplate.update(sql, parameters);
		}catch(Exception e) {
			log.error("unable to store metrics :{}", e.getMessage());
		}
		
		return status;
	}
}
