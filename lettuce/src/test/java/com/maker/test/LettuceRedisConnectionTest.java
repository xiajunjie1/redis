package com.maker.test;

import com.maker.redis.util.RedisConnectionPoolUtil;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lettuce提供的新的操作
 * */
public class LettuceRedisConnectionTest {
   //也可以替换成redis-socket


    private static final Logger LOGGER= LoggerFactory.getLogger(LettuceRedisConnectionTest.class);
    public static void main(String[] args) throws Exception{
        StatefulRedisConnection<String,String> redisConnection= RedisConnectionPoolUtil.getConnection();
       RedisAsyncCommands<String,String> commands = redisConnection.async();
      RedisFuture<String> setFuture = commands.set("xia20230906","helloworld");
       LOGGER.debug("【set操作执行结果】{}",setFuture.get());
       RedisFuture<String> getFuture = commands.get("xia20230906");
       LOGGER.debug("【get操作执行结果】{}",getFuture.get());
       redisConnection.close();//归还到连接池
    }
}
