package com.maker.test;

import com.maker.lock.StartApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest(classes = StartApplication.class)
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
public class testRedisTemplate {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private static final Logger LOGGER= LoggerFactory.getLogger(testRedisTemplate.class);
    @Test
    public void testRedis(){
        stringRedisTemplate.opsForValue().set("xia","test!!!");
        LOGGER.info("【redis数据】{}",stringRedisTemplate.opsForValue().get("xia"));
    }
}
