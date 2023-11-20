package com.maker.test;

import com.maker.redis.util.RedisSentinelConnectionUtils;
import io.lettuce.core.sentinel.api.StatefulRedisSentinelConnection;
import io.lettuce.core.sentinel.api.async.RedisSentinelAsyncCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LettuceSentinelTest {
    private static final Logger LOGGER= LoggerFactory.getLogger(LettuceSentinelTest.class);

    public static void main(String[] args) throws Exception{
        StatefulRedisSentinelConnection<String,String> connection=RedisSentinelConnectionUtils.getConnection();
        RedisSentinelAsyncCommands<String,String> commands=connection.async();
        LOGGER.info("【sentinel信息】{}",commands.info("sentinel").get());
        LOGGER.info("【master信息】{}",commands.master("mymaster").get());

}
}
