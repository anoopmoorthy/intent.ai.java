package com.bid.bservice.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

@Configuration
public class GeneralConfig {
	
    @Bean
    public ExecutorService executorService() {
        return Executors.newSingleThreadExecutor();
    }
    
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public ObjectMapper objectMapper() {
	    JavaTimeModule module = new JavaTimeModule();
	    
	    LocalDateTimeDeserializer localDateTimeDeserializer = 
	    		new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	    
	    module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
	    
	    ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
	            .modules(module)
	            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
	            .build();
	    
	    return objectMapper;
	}
}
