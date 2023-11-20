package com.maker.test;

import com.maker.redis.util.RedisConnectionPoolUtil;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisScriptingAsyncCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Lettuce执行lua脚本，实现商品的秒杀
 *
 * */
public class LettuceLuaSeckill {
    private static final Logger LOGGER= LoggerFactory.getLogger(LettuceLuaSeckill.class);
    public static void main(String[] args) throws Exception{

        try{
            //secKillTest();
            secKillThreadTest();
        }catch (Exception e){
            LOGGER.error("出现异常：{}",e.getMessage());
        }



    }

    private static void secKillTest() throws Exception{
        StatefulRedisConnection<String,String> redisConnection= RedisConnectionPoolUtil.getConnection();
        RedisScriptingAsyncCommands<String,String> commands = redisConnection.async();
        LOGGER.info("【lua脚本调用】秒杀结果：{}"
                ,commands.evalsha("5db3b73c2d01e188067e010ec2f6547ef88c3628"
                        ,ScriptOutputType.INTEGER,"jayjxia","1").get());
        TimeUnit.SECONDS.sleep(1);//模拟延迟
        redisConnection.close();
    }

    private static void secKillThreadTest() throws Exception{
        for(int x=0;x<50;x++){
            new Thread(()->{
               try {
                   StatefulRedisConnection<String,String> redisConnection= RedisConnectionPoolUtil.getConnection();
                   RedisScriptingAsyncCommands<String,String> commands = redisConnection.async();
                   LOGGER.info("【lua脚本调用】秒杀结果：{}"
                           ,commands.evalsha("5db3b73c2d01e188067e010ec2f6547ef88c3628"
                                   ,ScriptOutputType.INTEGER,Thread.currentThread().getName(),"1").get());
                   TimeUnit.SECONDS.sleep(5);//模拟延迟
                   redisConnection.close();
               }catch (Exception e){
                    LOGGER.error("【{}】出现异常：{}",Thread.currentThread().getName(),e.getMessage());
               }

            },"thread-kill-"+x).start();

        }

    }
}
