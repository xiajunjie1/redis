package com.maker.test;

import com.maker.spring.StartRedisApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = StartRedisApplication.class)
@ExtendWith(SpringExtension.class)
public class RedisClusterTest {
    private static final Logger LOGGER= LoggerFactory.getLogger(RedisClusterTest.class);
    @Autowired
    @Qualifier("clusterRedisTemplate")
    private RedisTemplate<String,String> clusterRedisTemplate;
    @Test
    public void clusterTest(){
        clusterRedisTemplate.opsForValue().set("xia:20231008","jayjxia111");
        LOGGER.info("【读取数据】{}",clusterRedisTemplate.opsForValue().get("xia:20231008"));
    }
}
