package com.maker.test;

import com.maker.redis.util.RedisConnectionPoolUtil;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Lettuce提供的Redis事务相关操作
 * */
public class LettuceTransactionTest {



    private static final Logger LOGGER= LoggerFactory.getLogger(LettuceTransactionTest.class);
    public static void main(String[] args) throws Exception{
        //transactionTest();
        //transactionExceptionTest();
        transactionThreadTest();

    }
    /**
     * 使用lettuce进行redis事务操作
     * */
    private static void transactionTest() throws Exception{
        StatefulRedisConnection<String,String> redisConnection= RedisConnectionPoolUtil.getConnection();
        RedisAsyncCommands<String,String> commands = redisConnection.async();
        commands.flushdb();
        commands.set("xia:message","jayjxia");
        commands.watch("xia:message");//设置数据的监控
        commands.multi(); //开启事务
        commands.set("xia:message","xiajj");
       // LOGGER.debug("【数据值】{}",commands.get("xia:message").get());
        TransactionResult result=commands.exec().get();
        result.forEach((data)->{
            LOGGER.debug("【事务提交结果】{}",data);
        });
        LOGGER.debug("【数据值】{}",commands.get("xia:message").get());
        redisConnection.close();//归还到连接池
    }

    /**
     * 事务操作中出现异常
     *  出现异常后会被Lettuce捕获，不会影响到后续的操作
     * */
    private static void transactionExceptionTest() throws Exception{
        StatefulRedisConnection<String,String> redisConnection= RedisConnectionPoolUtil.getConnection();
        RedisAsyncCommands<String,String> commands = redisConnection.async();
        commands.flushdb();
        commands.set("xia:message","jayjxia");
        commands.set("count","10");
        commands.watch("xia:message");//设置数据的监控
        commands.multi(); //开启事务
        commands.incr("xia:message");
        commands.incr("count");
        // LOGGER.debug("【数据值】{}",commands.get("xia:message").get());
        TransactionResult result=commands.exec().get();
        result.forEach((data)->{
            //会有两条结果，一条报错，一条为11
            LOGGER.debug("【事务提交结果】{}",data);
        });
        LOGGER.debug("【数据值】{}",commands.get("xia:message").get());
        LOGGER.debug("【数据值】{}",commands.get("count").get());
        redisConnection.close();//归还到连接池
    }

    /**
     * 进行多线程的操作
     * */
    private static void transactionThreadTest()throws Exception{
        StatefulRedisConnection<String,String> redisConnection= RedisConnectionPoolUtil.getConnection();
        RedisAsyncCommands<String,String> commands = redisConnection.async();
        commands.flushdb();
        commands.set("xia:count","10");
        startTransactionThread();
        updateDataThread();
    }
    /**
     * 开启事务的线程
     * */
    private static void startTransactionThread(){
        new Thread(()->{
            StatefulRedisConnection<String,String> redisConnection=null;
            try{
           redisConnection= RedisConnectionPoolUtil.getConnection();
            RedisAsyncCommands<String,String> commands = redisConnection.async();
            commands.watch("xia:count");
            commands.multi();
            commands.incrby("xia:count",10L);
            TimeUnit.SECONDS.sleep(3);
            TransactionResult result=commands.exec().get();
            result.forEach((res)->{
                LOGGER.debug("【{}】事务执行结果：{}",Thread.currentThread().getName(),res);
            });
            }catch (Exception e){
                LOGGER.error("【{}】出现异常：{}",Thread.currentThread().getName(),e.getMessage());
            }
            if (redisConnection!=null)redisConnection.close();

        },"startThread").start();
    }
    /**
     * 修改数据的线程
     * */
    private static void updateDataThread(){
        new Thread(()->{
            StatefulRedisConnection<String,String> redisConnection=null;
            try{
             redisConnection= RedisConnectionPoolUtil.getConnection();
            RedisAsyncCommands<String,String> commands = redisConnection.async();
           TimeUnit.SECONDS.sleep(1);
            Long result=commands.incrby("xia:count",30).get();
            LOGGER.debug("【{}】更新结果：{}",Thread.currentThread().getName(),result);
            }catch (Exception e){
                LOGGER.error("【{}】出现异常：{}",Thread.currentThread().getName(),e.getMessage());
            }
            if (redisConnection!=null)redisConnection.close();
        },"updateThread").start();
    }
}
