package com.maker.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisTemplateMasterSlaveConfig {

    @Bean("redisMasterTemplate")
    public RedisTemplate<String,Object> redisMasterTemplate(@Autowired RedisConnectionFactory lettuceMasterConnectionFactory){
        RedisTemplate<String,Object> redisTemplate=new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceMasterConnectionFactory);
        return redisTemplate;
    }

    @Bean("redisSlaveATemplate")
    public RedisTemplate<String,Object> redisSlaveATemplate(@Autowired RedisConnectionFactory lettuceSlaveAConnectionFactory){
        RedisTemplate<String,Object> redisTemplate=new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceSlaveAConnectionFactory);
        return redisTemplate;
    }

    @Bean("redisSlaveBTemplate")
    public RedisTemplate<String,Object> redisSlaveBTemplate(@Autowired RedisConnectionFactory lettuceSlaveBConnectionFactory){
        RedisTemplate<String,Object> redisTemplate=new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceSlaveBConnectionFactory);
        return redisTemplate;
    }

}
