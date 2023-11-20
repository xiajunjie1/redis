package com.maker.test;

import com.maker.redis.util.RedisConnectionPoolUtil;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 响应式命令
 * */
public class RedisReactiveCommandsTest {

    private static final Logger LOGGER= LoggerFactory.getLogger(RedisReactiveCommandsTest.class);
    public static void main(String[] args) throws Exception{
        StatefulRedisConnection<String,String> redisConnection= RedisConnectionPoolUtil.getConnection();
        RedisReactiveCommands<String,String> commands=redisConnection.reactive();
        //响应式命令返回的是一个Mono对象，采用观察者模式
        commands.flushdb().subscribe((result)->{
            //传入消费者，将生产者产生的数据进行处理
            LOGGER.debug("【清空redis数据库】{}",result);
        });
        commands.set("xia:2023910","reactiveTest").subscribe((result)->{
            LOGGER.debug("【向redis中存入数据】{}",result);
        });
        commands.get("xia:2023910").subscribe((result)->{
            LOGGER.debug("【从redis中获取数据】{}",result);
        });
       redisConnection.close();//归还到连接池
    }
}
