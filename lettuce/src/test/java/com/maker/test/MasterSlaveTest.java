package com.maker.test;

import com.maker.redis.util.RedisMasterSlaveClusterConnectionUtil;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MasterSlaveTest {
    private static final Logger LOGGER= LoggerFactory.getLogger(MasterSlaveTest.class);
    public static void main(String[] args) throws Exception{
       RedisAsyncCommands<String,String> commands = RedisMasterSlaveClusterConnectionUtil.getConnection().async();
        LOGGER.info("设置数据：{}",commands.set("xia:20230927","master write").get());
        LOGGER.info("读取数据：{}",commands.get("xia:20230927").get());
        RedisMasterSlaveClusterConnectionUtil.close();
    }
}
