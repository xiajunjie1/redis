package com.maker.spring.service.impl;

import com.maker.spring.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
@Service
public class RedisServiceImpl implements IRedisService {
    @Autowired
    @Qualifier("redisMasterTemplate")
    private RedisTemplate<String,Object> redisMasterTemplate;
    @Autowired
    @Qualifier("redisSlaveATemplate")
    private RedisTemplate<String,Object> redisSlaveATemplate;
    @Autowired
    @Qualifier("redisSlaveBTemplate")
    private RedisTemplate<String,Object> redisSlaveBTemplate;
    @Override
    public boolean add(String key, String value) {

        return redisMasterTemplate.opsForValue().setIfAbsent(key,value);
    }

    @Override
    public Set<String> search(String pattern) {
        return redisSlaveATemplate.keys(pattern);
    }

    @Override
    public String get(String key) {
        return redisSlaveBTemplate.opsForValue().get(key).toString();
    }
}
