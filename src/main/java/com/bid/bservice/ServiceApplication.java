package com.bid.bservice;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.bid.bservice.entity.AdCampaign;
import com.bid.bservice.entity.BidRequest;
import com.bid.bservice.entity.Exchange;
import com.bid.bservice.entity.MetricsExchange;
import com.bid.bservice.service.MetricsServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@EnableWebMvc
@ComponentScan(basePackages = "com.bid.bservice") 
public class ServiceApplication {
	
	public static String localhost;
	private static String bidRequestString = "{\"id\":\"1\",\"imp\":[{\"id\":\"1\",\"banner\":{\"w\":320,\"h\":240},\"ext\":{\"intent\":{\"placementId\":\"1\"}}},{\"id\":\"2\",\"banner\":{\"w\":300,\"h\":600},\"ext\":{\"intent\":{\"placementId\":\"2\"}}}],\"device\":{\"ua\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36\",\"geo\":{\"lat\":80,\"lon\":87,\"country\":\"US\",\"region\":\"CA\",\"metro\":\"SF\",\"city\":\"San Francisco\",\"zip\":\"94107\"},\"ip\":\"196.29.173.105\",\"devicetype\":2,\"os\":\"Mac OS X\"},\"user\":{\"id\":\"1\"},\"at\":0,\"bcat\":[\"IAB1-5\"]}";
	
	
	public static void main(String[] args) throws JsonMappingException, JsonProcessingException {
        try {
            localhost = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Host IP Address: " + localhost);
        } catch (UnknownHostException e) {
            System.err.println("Error fetching host IP address: " + e.getMessage());
        }
		//SpringApplication.run(ServiceApplication.class, args);
        
    System.out.println(bidRequestString);    
		
	}
}
