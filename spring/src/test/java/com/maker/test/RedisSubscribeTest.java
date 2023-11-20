package com.maker.test;

import com.maker.spring.StartRedisApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.TimeUnit;
/***
 * 由于Spring提供了相对应的监听类，所以只要保持Spring容器开启，就能收到发布的消息
 */

@ContextConfiguration(classes = StartRedisApplication.class)
@ExtendWith(SpringExtension.class)
public class RedisSubscribeTest {
    private static final Logger LOGGER= LoggerFactory.getLogger(RedisSubscribeTest.class);
    @Test
    public void testSubscribe() throws Exception{
        TimeUnit.SECONDS.sleep(Long.MAX_VALUE);//保持Spring容器一直开启

    }
}
