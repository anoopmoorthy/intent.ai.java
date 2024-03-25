package com.bid.bservice.config;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.bid.bservice.ServiceApplication;
import com.bid.bservice.entity.BidRequest;

import redis.embedded.RedisServer;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${runon.image}")
    private int image;
    
    private RedisServer redisServer;

    public void runsonimage() {
    	if(image == 1) {
    		redisHost = ServiceApplication.localhost;
    		redisPort = 6379;
    	}
    }
    
    @PostConstruct
    public void startRedisServer() throws IOException {
    	runsonimage();
        redisServer = new RedisServer();
        redisServer.start();
    }

    @PreDestroy
    public void stopRedisServer() throws IOException {
        redisServer.stop();
    }
    
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
    	RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
    	JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(config);
        //jedisConnectionFactory.setHostName(redisHost);
        //jeisConnectionFactory.setPort(redisPort);
        jedisConnectionFactory.getPoolConfig().setMaxIdle(30);
        jedisConnectionFactory.getPoolConfig().setMinIdle(10);
        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, BidRequest> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, BidRequest> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        RedisSerializer<BidRequest> jsonSerializer = new Jackson2JsonRedisSerializer<>(BidRequest.class);
        redisTemplate.setValueSerializer(jsonSerializer);
        redisTemplate.setHashValueSerializer(jsonSerializer);
        redisTemplate.setEnableTransactionSupport(true);
        return redisTemplate;
    }
    @Bean
    public RedisCacheManager cacheManager() {
        return RedisCacheManager.builder(redisConnectionFactory())
                .build();
    }
}
