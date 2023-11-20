package com.maker.test;

import com.maker.redis.util.RedisClusterConnectionPoolUtil;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisClusterTest {
    private static final Logger LOGGER= LoggerFactory.getLogger(RedisClusterTest.class);

    public static void main(String[] args)throws Exception {
        StatefulRedisClusterConnection<String,String> connection=RedisClusterConnectionPoolUtil.getRedisClusterConnection();
        RedisAdvancedClusterAsyncCommands<String,String> commands=connection.async();
        //LOGGER.info("【设置数据】{}",commands.set("xia:20231007","jayjxia").get());
        LOGGER.info("【获取数据】{}",commands.get("xia:20231007").get());
        connection.close();
    }
}
