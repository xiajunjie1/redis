package com.maker.test;

import com.maker.redis.util.RedisConnectionPoolUtil;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import io.lettuce.core.pubsub.api.reactive.RedisPubSubReactiveCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.Scanner;

/**
 * Lettuce发布者
 *  该程序实现发布者的功能
 * */
public class LettucePubishReactiveTest {
    private static final Logger LOGGER= LoggerFactory.getLogger(LettucePubishReactiveTest.class);

    public static void main(String[] args) throws Exception{
        StatefulRedisPubSubConnection<String,String> redisConnection= RedisConnectionPoolUtil
                .getRedisClient().connectPubSub();

       RedisPubSubReactiveCommands<String,String> commands = redisConnection.reactive();//创建异步命令
       Disposable disposable = Mono.just("nice to meet you").subscribe(data->{
            commands.publish("channel:xia",data).subscribe();
        });

        Scanner sc =new Scanner(System.in);
        sc.nextLine();
        disposable.dispose();

       redisConnection.close();//归还到连接池
    }
}
