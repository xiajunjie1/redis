package com.maker.test;

import com.maker.redis.util.RedisConnectionPoolUtil;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.async.RedisScriptingAsyncCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lettuce执行lua
 *
 * */
public class LettuceLuaTest {
    private static final Logger LOGGER= LoggerFactory.getLogger(LettuceLuaTest.class);
    public static void main(String[] args) throws Exception{
        StatefulRedisConnection<String,String> redisConnection= RedisConnectionPoolUtil.getConnection();
       RedisScriptingAsyncCommands<String,String> commands = redisConnection.async();
       String lua = """
                   for num=0,10,1 do
                        redis.call('SET',KEYS[1]..'message:'..num,'xiajj'..num)
                   end
                   return 'Data Create Successfully' --直接返回字符串
                   
               """;
       LOGGER.info("【脚本执行结果】{}",commands.eval(lua, ScriptOutputType.VALUE,"jayjxia").get());

       redisConnection.close();

    }
}
