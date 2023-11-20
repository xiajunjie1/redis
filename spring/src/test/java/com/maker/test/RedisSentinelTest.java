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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
/**
 * 配置信息采用的是哨兵的配置
 * 通过配置出来对应的template对哨兵集群进行数据操作
 * */
@ContextConfiguration(classes = StartRedisApplication.class)
@ExtendWith(SpringExtension.class)
public class RedisSentinelTest {
    @Autowired
    @Qualifier("sentinelRedisTemplate")
    private RedisTemplate<String,Object> sentinelRedisTemplate;
    private static final Logger LOGGER= LoggerFactory.getLogger(RedisSentinelTest.class);
    @Test
    public void sentinelTest(){
        sentinelRedisTemplate.execute(new RedisCallback<Object>() {

            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.execute("flushall");
                return null;
            }
        });
        sentinelRedisTemplate.opsForValue().set("xia","xiajj");
        LOGGER.info("【数据获取】xia={}",sentinelRedisTemplate.opsForValue().get("xia"));
    }
}
