package com.maker.test;

import com.maker.redis.util.RedisConnectionPoolUtil;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import io.lettuce.core.pubsub.api.reactive.RedisPubSubReactiveCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Disposable;

import java.util.Scanner;

/**
 * Lettuce基于响应式的发布订阅模式
 * 该程序为订阅者
 *  在基于响应式的方式进行订阅者的创建，不需要添加监听器，因为响应式本身就是基于观察者模式在
 *  一直监听
 * */
public class LettuceSubscribeReactiveTest {
    private static final Logger LOGGER= LoggerFactory.getLogger(LettuceSubscribeReactiveTest.class);

    public static void main(String[] args) throws Exception{
        StatefulRedisPubSubConnection<String,String> redisConnection= RedisConnectionPoolUtil
                .getRedisClient().connectPubSub();

       RedisPubSubReactiveCommands<String,String> commands = redisConnection.reactive();//创建响应式命令
      commands.subscribe("channel:xia").block();//设置监听的通道
     Disposable disposable = commands.observeChannels()
               .doOnNext(msg -> { //针对监听的通道进行配置
                LOGGER.info("【接收订阅消息】通道：{}、消息：{}",msg.getChannel(),msg.getMessage());
       }).subscribe();
       System.out.println("开启消息订阅服务，监听channel:xia通道");
        Scanner sc=new Scanner(System.in);
        sc.nextLine();//键盘任意输入，阻塞整个进程，让订阅者可以一直监听是否收到消息
        commands.unsubscribe("channel:xia");//退订
        disposable.dispose();//关闭订阅处理
        redisConnection.close();//归还到连接池
    }
}
