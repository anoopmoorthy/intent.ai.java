package com.bid.bservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.bid.bservice.dao.MetricsDao;

@ExtendWith(SpringExtension.class)
public class MetricsServiceImplTest {
	
	@Mock 
	private MetricsDao metricsDao;
	
	@Test
	public void testcalculateMetrics() {
		
	}
}
