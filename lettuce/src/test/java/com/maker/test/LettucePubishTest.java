package com.maker.test;

import com.maker.redis.util.RedisConnectionPoolUtil;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * Lettuce发布者
 *  该程序实现发布者的功能
 * */
public class LettucePubishTest {
    private static final Logger LOGGER= LoggerFactory.getLogger(LettucePubishTest.class);

    public static void main(String[] args) throws Exception{
        StatefulRedisPubSubConnection<String,String> redisConnection= RedisConnectionPoolUtil
                .getRedisClient().connectPubSub();

       RedisPubSubAsyncCommands<String,String> commands = redisConnection.async();//创建异步命令
        commands.publish("channel:xia","nice to meet you");

       redisConnection.close();//归还到连接池
    }
}
