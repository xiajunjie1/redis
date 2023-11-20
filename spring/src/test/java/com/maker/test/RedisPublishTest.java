package com.maker.test;

import com.maker.spring.StartRedisApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.TimeUnit;

/***
 * RedisTemplate内部提供了发布者的相关操作
 */

@ContextConfiguration(classes = StartRedisApplication.class)
@ExtendWith(SpringExtension.class)
public class RedisPublishTest {
    private static final Logger LOGGER= LoggerFactory.getLogger(RedisPublishTest.class);
    @Autowired
    private RedisTemplate<String,String> stringRedisTemplate;
    @Test
    public void testPublish() throws Exception{
        //进行消息的发布
        this.stringRedisTemplate.convertAndSend("channel:xia","hello world");

    }
}
