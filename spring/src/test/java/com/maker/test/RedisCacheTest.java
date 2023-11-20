package com.maker.test;

import com.maker.spring.StartRedisApplication;
import com.maker.spring.service.IMessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ContextConfiguration(classes = {StartRedisApplication.class})
@ExtendWith(SpringExtension.class)
public class RedisCacheTest {
    private static final Logger LOGGER= LoggerFactory.getLogger(RedisCacheTest.class);
    @Autowired
    private IMessageService messageService;
    @Test
    public void redisCacheTest(){
       List<String> result= messageService.findAll();
       LOGGER.info("【获取数据结果】{}",result);
    }
}
