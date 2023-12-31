package com.maker.reactor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
public class LettuceConfig {
    @Bean
    public ReactiveRedisTemplate<String,String> reactiveRedisTemplate(@Autowired ReactiveRedisConnectionFactory reactiveRedisConnectionFactory){
        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, RedisSerializationContext.string());
    }
}
