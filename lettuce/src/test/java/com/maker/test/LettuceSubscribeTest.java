package com.maker.test;

import com.maker.redis.util.RedisConnectionPoolUtil;
import io.lettuce.core.RedisConnectionStateAdapter;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import io.lettuce.core.pubsub.api.reactive.RedisPubSubReactiveCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * Lettuce发布订阅模式
 * 该程序为订阅者
 *  对于订阅者而言，需要给连接加上一个监听器，对接收到的消息进行处理
 * */
public class LettuceSubscribeTest {
    private static final Logger LOGGER= LoggerFactory.getLogger(LettuceSubscribeTest.class);

    public static void main(String[] args) throws Exception{
        StatefulRedisPubSubConnection<String,String> redisConnection= RedisConnectionPoolUtil
                .getRedisClient().connectPubSub();
       redisConnection.addListener(new RedisPubSubAdapter<>() {
           @Override
           public void message(String channel, String message) {
               LOGGER.info("【消息订阅】通道：{}，消息：{}",channel,message);
           }
       });
       RedisPubSubAsyncCommands<String,String> commands = redisConnection.async();//创建异步命令
      commands.subscribe("channel:xia");//设置监听的通道
        Scanner sc=new Scanner(System.in);
        sc.nextLine();//键盘任意输入，阻塞整个进程，让订阅者可以一直监听是否收到消息
        commands.unsubscribe("channel:xia");//退订
       redisConnection.close();//归还到连接池
    }
}
