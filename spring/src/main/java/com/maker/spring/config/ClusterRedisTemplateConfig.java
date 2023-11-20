package com.maker.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class ClusterRedisTemplateConfig {
    @Bean("clusterRedisTemplate")
    public RedisTemplate<String,String> clusterRedisTemplate(
            @Autowired
            @Qualifier("clusterConnectionFactory")
            LettuceConnectionFactory clusterConnectionFactory){
        RedisTemplate<String,String> redisTemplate=new RedisTemplate<>();
        redisTemplate.setConnectionFactory(clusterConnectionFactory);
       //redisTemplate.setKeySerializer();
        return redisTemplate;
    }
}
