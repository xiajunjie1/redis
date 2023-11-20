package com.maker.test;

import com.maker.spring.StartRedisApplication;
import com.maker.spring.service.IRedisService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = StartRedisApplication.class)
@ExtendWith(SpringExtension.class)
public class RedisMasterSlaveTest {
    @Autowired
    private IRedisService redisService;
    private static final Logger LOGGER= LoggerFactory.getLogger(RedisMasterSlaveTest.class);
    @Test
    public void masterSlaveTest(){
        LOGGER.info("【新增操作】{}",redisService.add("xia:20230928","xiajayj"));
        LOGGER.info("【匹配查询key】{}",redisService.search("xia:*"));
        LOGGER.info("【获取数据】{}",redisService.get("xia:20230928"));
    }
}
