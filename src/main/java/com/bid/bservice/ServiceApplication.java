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
	
	public static void main(String[] args) throws JsonMappingException, JsonProcessingException {
        try {
            localhost = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Host IP Address: " + localhost);
        } catch (UnknownHostException e) {
            System.err.println("Error fetching host IP address: " + e.getMessage());
        }
		SpringApplication.run(ServiceApplication.class, args);
	}
}
