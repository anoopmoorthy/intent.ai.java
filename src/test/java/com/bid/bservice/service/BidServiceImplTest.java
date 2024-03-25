package com.bid.bservice.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.bid.bservice.dao.BidDao;
import com.bid.bservice.entity.AdCampaign;
import com.bid.bservice.entity.BidRequest;
import com.bid.bservice.entity.Exchange;
import com.bid.bservice.entity.MetricsExchange;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
public class BidServiceImplTest {
	
	@Mock
	private MetricsService metricsService;
	@Mock
	private BidDao bidDao;
	@Mock
	private RedisService redisService;
	@Mock
	private ExecutorService executor;

	private ObjectMapper objectMapper = new ObjectMapper();
	
	@InjectMocks
	BidServiceImpl bidService;
	
	private static String bidRequestString = "{\"id\":\"1\",\"imp\":[{\"id\":\"1\",\"banner\":{\"w\":320,\"h\":240},\"ext\":{\"intent\":{\"placementId\":\"1\"}}},{\"id\":\"2\",\"banner\":{\"w\":300,\"h\":600},\"ext\":{\"intent\":{\"placementId\":\"2\"}}}],\"device\":{\"ua\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36\",\"geo\":{\"lat\":80,\"lon\":87,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"ip\":\"196.29.173.105\",\"devicetype\":2,\"os\":\"Mac OS X\"},\"user\":{\"id\":\"1\"},\"at\":0,\"bcat\":[\"IAB1-5\"]}";
	private static String adCampaignsListString = "[{\"id\":\"2\",\"name\":\"Fitness Challenge Promo\",\"startDate\":null,\"endDate\":null,\"budget\":0.54,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":300,\"h\":250},\"ext\":{\"intent\":{\"placementId\":\"1\"}}},\"placementIds\":[1],\"iab\":[\"IAB4-6\"],\"targeting\":{\"geo\":{\"lat\":36,\"lon\":3,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":2,\"os\":\"Windows\"}},\"creatives\":null},{\"id\":\"8\",\"name\":\"Spring Collection Launch\",\"startDate\":null,\"endDate\":null,\"budget\":0.52,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":300,\"h\":250},\"ext\":{\"intent\":{\"placementId\":\"1\"}}},\"placementIds\":[1],\"iab\":[\"IAB4\"],\"targeting\":{\"geo\":{\"lat\":45,\"lon\":90,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":2,\"os\":\"Mac OS X\"}},\"creatives\":null},{\"id\":\"1\",\"name\":\"Fitness Challenge Promo\",\"startDate\":null,\"endDate\":null,\"budget\":0.68,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":300,\"h\":250},\"ext\":{\"intent\":{\"placementId\":\"2\"}}},\"placementIds\":[2],\"iab\":[\"IAB8-11\"],\"targeting\":{\"geo\":{\"lat\":97,\"lon\":89,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":1,\"os\":\"Windows\"}},\"creatives\":null},{\"id\":\"9\",\"name\":\"Spring Collection Launch\",\"startDate\":null,\"endDate\":null,\"budget\":0.74,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":300,\"h\":250},\"ext\":{\"intent\":{\"placementId\":\"1\"}}},\"placementIds\":[2],\"iab\":[\"IAB2-10\"],\"targeting\":{\"geo\":{\"lat\":98,\"lon\":52,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":1,\"os\":\"Mac OS X\"}},\"creatives\":null},{\"id\":\"9\",\"name\":\"Fitness Challenge Promo\",\"startDate\":null,\"endDate\":null,\"budget\":0.69,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":300,\"h\":600},\"ext\":{\"intent\":{\"placementId\":\"2\"}}},\"placementIds\":[2],\"iab\":[\"IAB3-2\"],\"targeting\":{\"geo\":{\"lat\":71,\"lon\":61,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":2,\"os\":\"Windows\"}},\"creatives\":null},{\"id\":\"2\",\"name\":\"Travel Destination Offers\",\"startDate\":null,\"endDate\":null,\"budget\":0.52,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":300,\"h\":250},\"ext\":{\"intent\":{\"placementId\":\"2\"}}},\"placementIds\":[2],\"iab\":[\"IAB3-1\"],\"targeting\":{\"geo\":{\"lat\":31,\"lon\":31,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":1,\"os\":\"Windows\"}},\"creatives\":null},{\"id\":\"2\",\"name\":\"Spring Collection Launch\",\"startDate\":null,\"endDate\":null,\"budget\":0.57,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":320,\"h\":240},\"ext\":{\"intent\":{\"placementId\":\"2\"}}},\"placementIds\":[2],\"iab\":[\"IAB6-2\"],\"targeting\":{\"geo\":{\"lat\":75,\"lon\":48,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":1,\"os\":\"Mac OS X\"}},\"creatives\":null},{\"id\":\"10\",\"name\":\"Back-to-School Deals\",\"startDate\":null,\"endDate\":null,\"budget\":0.63,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":300,\"h\":250},\"ext\":{\"intent\":{\"placementId\":\"1\"}}},\"placementIds\":[2],\"iab\":[\"IAB9-19\"],\"targeting\":{\"geo\":{\"lat\":39,\"lon\":67,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":1,\"os\":\"Mac OS X\"}},\"creatives\":null},{\"id\":\"2\",\"name\":\"Spring Collection Launch\",\"startDate\":null,\"endDate\":null,\"budget\":0.52,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":300,\"h\":600},\"ext\":{\"intent\":{\"placementId\":\"1\"}}},\"placementIds\":[2],\"iab\":[\"IAB9-9\"],\"targeting\":{\"geo\":{\"lat\":76,\"lon\":29,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":1,\"os\":\"Windows\"}},\"creatives\":null},{\"id\":\"10\",\"name\":\"Fitness Challenge Promo\",\"startDate\":null,\"endDate\":null,\"budget\":0.54,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":320,\"h\":240},\"ext\":{\"intent\":{\"placementId\":\"2\"}}},\"placementIds\":[2],\"iab\":[\"IAB6-2\"],\"targeting\":{\"geo\":{\"lat\":74,\"lon\":69,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":2,\"os\":\"Windows\"}},\"creatives\":null},{\"id\":\"4\",\"name\":\"Food Festival Discounts\",\"startDate\":null,\"endDate\":null,\"budget\":0.79,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":320,\"h\":240},\"ext\":{\"intent\":{\"placementId\":\"2\"}}},\"placementIds\":[1],\"iab\":[\"IAB9-20\"],\"targeting\":{\"geo\":{\"lat\":9,\"lon\":16,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":1,\"os\":\"Mac OS X\"}},\"creatives\":null},{\"id\":\"7\",\"name\":\"Winter Clearance\",\"startDate\":null,\"endDate\":null,\"budget\":0.62,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":320,\"h\":240},\"ext\":{\"intent\":{\"placementId\":\"1\"}}},\"placementIds\":[2],\"iab\":[\"IAB3-8\"],\"targeting\":{\"geo\":{\"lat\":98,\"lon\":3,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":2,\"os\":\"Mac OS X\"}},\"creatives\":null},{\"id\":\"7\",\"name\":\"Food Festival Discounts\",\"startDate\":null,\"endDate\":null,\"budget\":0.71,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":320,\"h\":480},\"ext\":{\"intent\":{\"placementId\":\"2\"}}},\"placementIds\":[2],\"iab\":[\"IAB1-3\"],\"targeting\":{\"geo\":{\"lat\":99,\"lon\":46,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":2,\"os\":\"Windows\"}},\"creatives\":null},{\"id\":\"3\",\"name\":\"Back-to-School Deals\",\"startDate\":null,\"endDate\":null,\"budget\":0.76,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":320,\"h\":240},\"ext\":{\"intent\":{\"placementId\":\"2\"}}},\"placementIds\":[2],\"iab\":[\"IAB4\"],\"targeting\":{\"geo\":{\"lat\":77,\"lon\":24,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":1,\"os\":\"Windows\"}},\"creatives\":null},{\"id\":\"2\",\"name\":\"New Year's Discounts\",\"startDate\":null,\"endDate\":null,\"budget\":0.53,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":320,\"h\":240},\"ext\":{\"intent\":{\"placementId\":\"1\"}}},\"placementIds\":[2],\"iab\":[\"IAB3-10\"],\"targeting\":{\"geo\":{\"lat\":4,\"lon\":46,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":2,\"os\":\"Mac OS X\"}},\"creatives\":null},{\"id\":\"4\",\"name\":\"Fitness Challenge Promo\",\"startDate\":null,\"endDate\":null,\"budget\":0.68,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":300,\"h\":250},\"ext\":{\"intent\":{\"placementId\":\"1\"}}},\"placementIds\":[2],\"iab\":[\"IAB5-10\"],\"targeting\":{\"geo\":{\"lat\":31,\"lon\":60,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":2,\"os\":\"Mac OS X\"}},\"creatives\":null},{\"id\":\"3\",\"name\":\"Holiday Specials\",\"startDate\":null,\"endDate\":null,\"budget\":0.63,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":320,\"h\":480},\"ext\":{\"intent\":{\"placementId\":\"2\"}}},\"placementIds\":[2],\"iab\":[\"IAB5-11\"],\"targeting\":{\"geo\":{\"lat\":16,\"lon\":67,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":2,\"os\":\"Mac OS X\"}},\"creatives\":null},{\"id\":\"9\",\"name\":\"Winter Clearance\",\"startDate\":null,\"endDate\":null,\"budget\":0.74,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":300,\"h\":600},\"ext\":{\"intent\":{\"placementId\":\"1\"}}},\"placementIds\":[1],\"iab\":[\"IAB7-7\"],\"targeting\":{\"geo\":{\"lat\":39,\"lon\":87,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":2,\"os\":\"Windows\"}},\"creatives\":null},{\"id\":\"2\",\"name\":\"Back-to-School Deals\",\"startDate\":null,\"endDate\":null,\"budget\":0.56,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":320,\"h\":480},\"ext\":{\"intent\":{\"placementId\":\"2\"}}},\"placementIds\":[1],\"iab\":[\"IAB6\"],\"targeting\":{\"geo\":{\"lat\":21,\"lon\":42,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":1,\"os\":\"Windows\"}},\"creatives\":null},{\"id\":\"8\",\"name\":\"Summer Sale 2024\",\"startDate\":null,\"endDate\":null,\"budget\":0.6,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":320,\"h\":240},\"ext\":{\"intent\":{\"placementId\":\"1\"}}},\"placementIds\":[2],\"iab\":[\"IAB8-2\"],\"targeting\":{\"geo\":{\"lat\":34,\"lon\":90,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":1,\"os\":\"Windows\"}},\"creatives\":null},{\"id\":\"8\",\"name\":\"Back-to-School Deals\",\"startDate\":null,\"endDate\":null,\"budget\":0.72,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":300,\"h\":600},\"ext\":{\"intent\":{\"placementId\":\"2\"}}},\"placementIds\":[1],\"iab\":[\"IAB1-5\"],\"targeting\":{\"geo\":{\"lat\":57,\"lon\":73,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":2,\"os\":\"Windows\"}},\"creatives\":null},{\"id\":\"9\",\"name\":\"Holiday Specials\",\"startDate\":null,\"endDate\":null,\"budget\":0.77,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":320,\"h\":480},\"ext\":{\"intent\":{\"placementId\":\"2\"}}},\"placementIds\":[1],\"iab\":[\"IAB5-12\"],\"targeting\":{\"geo\":{\"lat\":32,\"lon\":78,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":2,\"os\":\"Mac OS X\"}},\"creatives\":null},{\"id\":\"6\",\"name\":\"Spring Collection Launch\",\"startDate\":null,\"endDate\":null,\"budget\":0.71,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":300,\"h\":250},\"ext\":{\"intent\":{\"placementId\":\"1\"}}},\"placementIds\":[1],\"iab\":[\"IAB3-5\"],\"targeting\":{\"geo\":{\"lat\":61,\"lon\":34,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":2,\"os\":\"Mac OS X\"}},\"creatives\":null},{\"id\":\"3\",\"name\":\"New Year's Discounts\",\"startDate\":null,\"endDate\":null,\"budget\":0.68,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":320,\"h\":240},\"ext\":{\"intent\":{\"placementId\":\"1\"}}},\"placementIds\":[1],\"iab\":[\"IAB3-10\"],\"targeting\":{\"geo\":{\"lat\":91,\"lon\":70,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":2,\"os\":\"Mac OS X\"}},\"creatives\":null},{\"id\":\"7\",\"name\":\"Winter Clearance\",\"startDate\":null,\"endDate\":null,\"budget\":0.68,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":300,\"h\":600},\"ext\":{\"intent\":{\"placementId\":\"1\"}}},\"placementIds\":[2],\"iab\":[\"IAB3-11\"],\"targeting\":{\"geo\":{\"lat\":59,\"lon\":72,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":1,\"os\":\"Mac OS X\"}},\"creatives\":null},{\"id\":\"8\",\"name\":\"Winter Clearance\",\"startDate\":null,\"endDate\":null,\"budget\":0.72,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":300,\"h\":600},\"ext\":{\"intent\":{\"placementId\":\"2\"}}},\"placementIds\":[2],\"iab\":[\"IAB4-9\"],\"targeting\":{\"geo\":{\"lat\":41,\"lon\":69,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":2,\"os\":\"Windows\"}},\"creatives\":null},{\"id\":\"9\",\"name\":\"Holiday Specials\",\"startDate\":null,\"endDate\":null,\"budget\":0.76,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":300,\"h\":250},\"ext\":{\"intent\":{\"placementId\":\"2\"}}},\"placementIds\":[2],\"iab\":[\"IAB4-4\"],\"targeting\":{\"geo\":{\"lat\":53,\"lon\":25,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":2,\"os\":\"Windows\"}},\"creatives\":null},{\"id\":\"6\",\"name\":\"Winter Clearance\",\"startDate\":null,\"endDate\":null,\"budget\":0.69,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":320,\"h\":480},\"ext\":{\"intent\":{\"placementId\":\"1\"}}},\"placementIds\":[2],\"iab\":[\"IAB2-3\"],\"targeting\":{\"geo\":{\"lat\":41,\"lon\":2,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":1,\"os\":\"Windows\"}},\"creatives\":null},{\"id\":\"2\",\"name\":\"Spring Collection Launch\",\"startDate\":null,\"endDate\":null,\"budget\":0.8,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":320,\"h\":480},\"ext\":{\"intent\":{\"placementId\":\"1\"}}},\"placementIds\":[1],\"iab\":[\"IAB3-11\"],\"targeting\":{\"geo\":{\"lat\":51,\"lon\":86,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":2,\"os\":\"Mac OS X\"}},\"creatives\":null},{\"id\":\"2\",\"name\":\"Back-to-School Deals\",\"startDate\":null,\"endDate\":null,\"budget\":0.58,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":320,\"h\":240},\"ext\":{\"intent\":{\"placementId\":\"1\"}}},\"placementIds\":[1],\"iab\":[\"IAB8\"],\"targeting\":{\"geo\":{\"lat\":33,\"lon\":92,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":1,\"os\":\"Mac OS X\"}},\"creatives\":null},{\"id\":\"4\",\"name\":\"New Year's Discounts\",\"startDate\":null,\"endDate\":null,\"budget\":0.78,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":320,\"h\":240},\"ext\":{\"intent\":{\"placementId\":\"1\"}}},\"placementIds\":[1],\"iab\":[\"IAB9-11\"],\"targeting\":{\"geo\":{\"lat\":5,\"lon\":37,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":1,\"os\":\"Windows\"}},\"creatives\":null},{\"id\":\"9\",\"name\":\"Fitness Challenge Promo\",\"startDate\":null,\"endDate\":null,\"budget\":0.58,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":320,\"h\":240},\"ext\":{\"intent\":{\"placementId\":\"2\"}}},\"placementIds\":[2],\"iab\":[\"IAB5-5\"],\"targeting\":{\"geo\":{\"lat\":53,\"lon\":59,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":1,\"os\":\"Mac OS X\"}},\"creatives\":null},{\"id\":\"5\",\"name\":\"Holiday Specials\",\"startDate\":null,\"endDate\":null,\"budget\":0.64,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":300,\"h\":600},\"ext\":{\"intent\":{\"placementId\":\"1\"}}},\"placementIds\":[1],\"iab\":[\"IAB2-7\"],\"targeting\":{\"geo\":{\"lat\":96,\"lon\":99,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":1,\"os\":\"Mac OS X\"}},\"creatives\":null},{\"id\":\"6\",\"name\":\"Summer Sale 2024\",\"startDate\":null,\"endDate\":null,\"budget\":0.53,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":320,\"h\":240},\"ext\":{\"intent\":{\"placementId\":\"2\"}}},\"placementIds\":[2],\"iab\":[\"IAB1-7\"],\"targeting\":{\"geo\":{\"lat\":41,\"lon\":3,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":1,\"os\":\"Windows\"}},\"creatives\":null},{\"id\":\"5\",\"name\":\"Fitness Challenge Promo\",\"startDate\":null,\"endDate\":null,\"budget\":0.6,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":300,\"h\":600},\"ext\":{\"intent\":{\"placementId\":\"1\"}}},\"placementIds\":[2],\"iab\":[\"IAB4-11\"],\"targeting\":{\"geo\":{\"lat\":9,\"lon\":74,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":1,\"os\":\"Mac OS X\"}},\"creatives\":null},{\"id\":\"5\",\"name\":\"Back-to-School Deals\",\"startDate\":null,\"endDate\":null,\"budget\":0.75,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":300,\"h\":600},\"ext\":{\"intent\":{\"placementId\":\"2\"}}},\"placementIds\":[2],\"iab\":[\"IAB7-3\"],\"targeting\":{\"geo\":{\"lat\":45,\"lon\":80,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":2,\"os\":\"Windows\"}},\"creatives\":null},{\"id\":\"1\",\"name\":\"Back-to-School Deals\",\"startDate\":null,\"endDate\":null,\"budget\":0.58,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":320,\"h\":480},\"ext\":{\"intent\":{\"placementId\":\"2\"}}},\"placementIds\":[1],\"iab\":[\"IAB3-5\"],\"targeting\":{\"geo\":{\"lat\":14,\"lon\":14,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":1,\"os\":\"Mac OS X\"}},\"creatives\":null},{\"id\":\"2\",\"name\":\"Travel Destination Offers\",\"startDate\":null,\"endDate\":null,\"budget\":0.64,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":320,\"h\":480},\"ext\":{\"intent\":{\"placementId\":\"1\"}}},\"placementIds\":[1],\"iab\":[\"IAB8-18\"],\"targeting\":{\"geo\":{\"lat\":84,\"lon\":6,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":2,\"os\":\"Windows\"}},\"creatives\":null}]";
	private static String winningCampaign = "{\"id\":\"5\",\"name\":\"Back-to-School Deals\",\"startDate\":null,\"endDate\":null,\"budget\":0.75,\"status\":null,\"imp\":{\"id\":\"\",\"banner\":{\"w\":300,\"h\":600},\"ext\":{\"intent\":{\"placementId\":\"2\"}}},\"placementIds\":[2],\"iab\":[\"IAB7-3\"],\"targeting\":{\"geo\":{\"lat\":45,\"lon\":80,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"deviceType\":{\"devicetype\":2,\"os\":\"Windows\"}},\"creatives\":null}";
	private BidRequest bidRequest;
	private List<AdCampaign> adCampaigns;
	private AdCampaign campaign;
	private MetricsExchange metricsExchange;
	private Exchange exchange;
	
	@BeforeEach
	public void initialize() throws JsonMappingException, JsonProcessingException {
		bidRequest = objectMapper.readValue(bidRequestString, new TypeReference<BidRequest>(){});
		adCampaigns = objectMapper.readValue(adCampaignsListString, new TypeReference<List<AdCampaign>>(){});
		campaign = objectMapper.readValue(winningCampaign, new TypeReference<AdCampaign>(){});
		metricsExchange = new MetricsExchange(0.80, 0.52, 38.0, 0.6521052631578947, "IAB7-3)");
		exchange = new Exchange(bidRequest, campaign, adCampaigns, metricsExchange);
	}
	@Test
	public void testaddNewBidRequest() {
		
		when(redisService.enqueueJsonData(Mockito.any(BidRequest.class)))
			.thenReturn(10L);
		
		Long index = bidService.addNewBidRequest(bidRequest);
		
		Assertions.assertThat(index).isEqualTo(10L);
	}
	@Test
	public void testcurrentBidStatus() throws IOException {
		/*
		when(redisService.dequeueJsonData()).thenReturn(bidRequest);
		//when(metricsService.calculateMetrics(bidRequest, adCampaigns, campaign)).thenReturn(metricsExchange);
		//when(bidService.matchBidRequestToCampaign(bidRequest)).thenReturn(exchange);
		//when(objectMapper.writeValueAsString(Mockito.mock(Exchange.class))).thenReturn("");
		SseEmitter emitter = new SseEmitter();
		
		bidService.currentBidStatus(emitter);
		
		verify(emitter, times(1)).send(any(SseEmitter.SseEventBuilder.class));
		*/
	}
	@Test
	public void testmatchBidRequestToCampaign() {
		
		BidRequest bidRequestMock = mock(BidRequest.class);
		List<AdCampaign> adCampaignListMock = mock(List.class);
		AdCampaign adCampaignMock = mock(AdCampaign.class);
		
		when(bidDao.generateAdCampaigns())
			.thenReturn(adCampaigns);
		when(metricsService.calculateMetrics(bidRequest, adCampaigns, campaign))
			.thenReturn(metricsExchange);
		
		Exchange exchange = bidService.matchBidRequestToCampaign(bidRequest);
		
		Assertions
		.assertThat(bidRequest).matches(bid -> bid.getDevice().getIp().equals("196.29.173.105") &&
				bid.getBcat().get(0).equals("IAB1-5") &&
				bid.getImp().get(0).getBanner().getW().equals(320) &&
				bid.getImp().get(0).getBanner().getH().equals(240));
		Assertions.assertThat(exchange.getWinningCampaign()).matches(win -> 
				win.getImp().getBanner().getW().equals(300) &&
				win.getImp().getBanner().getH().equals(600) &&
				!win.getIab().equals("IAB1-5") &&
				win.getImp().getExt().getIntent().getPlacementId().equals("2"));
	}
	
}