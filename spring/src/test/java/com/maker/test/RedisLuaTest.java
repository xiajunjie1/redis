package com.maker.test;

import com.maker.spring.StartRedisApplication;
import com.maker.spring.service.ILimitService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.TimeUnit;

@ContextConfiguration(classes = StartRedisApplication.class)
@ExtendWith(SpringExtension.class)
public class RedisLuaTest {
    private static final Logger LOGGER= LoggerFactory.getLogger(RedisLuaTest.class);
    @Autowired
    private ILimitService limitService;
    @Test
    public void getTest(){
        //模拟同一ip多次访问被限流的业务方法
        for(int x=0;x<10;x++){
            new Thread(()->{
                LOGGER.info("【业务调用】{}",limitService.get());
            },"Thread-"+x).start();
        }
        try{
        TimeUnit.SECONDS.sleep(5);//保证所有线程执行完
    }catch (Exception e){
            e.printStackTrace();
        }
    }
}
