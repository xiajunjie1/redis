package com.maker.test;

import com.maker.spring.StartRedisApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = StartRedisApplication.class)
@ExtendWith(SpringExtension.class)
public class RedisFunctionTest {
    @Autowired
    @Qualifier("stringRedisTemplate2")
    private StringRedisTemplate stringRedisTemplate;
    private static final Logger LOGGER= LoggerFactory.getLogger(RedisLuaTest.class);
    @Test
    public void functionTest(){
        stringRedisTemplate.execute(new RedisCallback<String>() {

            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {

               Object result= connection.execute("FCALL","info".getBytes()
                        ,"0".getBytes(),"xia123".getBytes(),"fuck world".getBytes());
                LOGGER.info("【函数调用】{}",new String((byte[]) result));
               return null;
            }
        });
    }
}
