package com.maker.test;

import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.StringCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

/**
 * Lettuce执行redis命令
 * */
public class LettuceTest {
   //也可以替换成redis-socket
    private static final String REDIS_STRING="redis://default:xia123@192.168.88.135:6379/0";//redis连接字符串

    private static final Logger LOGGER= LoggerFactory.getLogger(LettuceTest.class);
    public static void main(String[] args) throws Exception {
        RedisURI uri=RedisURI.create(REDIS_STRING);

        RedisClient redisClient=RedisClient.create(uri);
        //getAsyncCommand(redisClient);
        //getAsyncCommandListener(redisClient);
        getAsyncConnection();

    }

   /**
    * 获取异步的redis命令对象
    * */
    private static void getAsyncCommand(RedisClient redisClient) throws Exception{
        StatefulRedisConnection<String,String> statefulRedisConnection= redisClient.connect();//获取连接
        RedisAsyncCommands<String,String> redisCommands= statefulRedisConnection.async();
        //具体的redis命令被包装成了方法，由于是异步操作，开启了一个子线程，所以返回的是一个future对象，用来携带子线程返回的结果
     RedisFuture<String> setFuture = redisCommands.set("jayjxia","hello");
     LOGGER.debug("【set命令执行结果】{}",setFuture.get());
     RedisFuture<String> getFuture = redisCommands.get("jayjxia");
     LOGGER.debug("【get命令执行结果】{}",getFuture.get());
     statefulRedisConnection.close();//关闭操作
    }
    /**
     * 为我们的操作添加监听机制
     * */
    private static void getAsyncCommandListener(RedisClient redisClient) throws Exception{
        StatefulRedisConnection<String,String> statefulRedisConnection= redisClient.connect();//获取连接
       statefulRedisConnection.addListener(new RedisConnectionStateListener() {
           @Override
           public void onRedisConnected(RedisChannelHandler<?, ?> connection, SocketAddress socketAddress) {
               LOGGER.debug("【redis连接监听】连接上了redis的服务");
           }

           @Override
           public void onRedisDisconnected(RedisChannelHandler<?, ?> connection) {
               LOGGER.debug("【redis连接监听】断开了redis的服务");
           }

           @Override
           public void onRedisExceptionCaught(RedisChannelHandler<?, ?> connection, Throwable cause) {
               LOGGER.error("【redis连接监听】redis操作出现了一个异常:{}",cause.getMessage());
           }
       });
        RedisAsyncCommands<String,String> redisCommands= statefulRedisConnection.async();
        //具体的redis命令被包装成了方法，由于是异步操作，开启了一个子线程，所以返回的是一个future对象，用来携带子线程返回的结果
        RedisFuture<String> setFuture = redisCommands.set("jayjxia","hello");
        LOGGER.debug("【set命令执行结果】{}",setFuture.get());
        RedisFuture<String> getFuture = redisCommands.get("jayjxia");
        LOGGER.debug("【get命令执行结果】{}",getFuture.get());
        statefulRedisConnection.close();//关闭操作
    }

    /**
     * 在获取redis连接时，如果未获取到该连接那么则会一直等待，如果考虑到一些特殊的网络环境设置，有可能出现
     * 较为大的连接问题，考虑到这种情况，lettuce中有提供一个连接超时的参数，也提供有一个异步的获取连接的方法
     * */
    private static void getAsyncConnection() throws Exception{
        RedisURI uri=RedisURI.create(REDIS_STRING);
        RedisClient redisClient=RedisClient.create(uri);
        //从获取连接开始，就是进行的异步获取
        ConnectionFuture<StatefulRedisConnection<String,String>> connectionFuture= redisClient.connectAsync(new StringCodec(),uri);//获取连接
        //通过得到的future，进行在获取Redis连接
        StatefulRedisConnection<String,String> statefulRedisConnection=connectionFuture.get();
        RedisAsyncCommands<String,String> redisCommands= statefulRedisConnection.async();

        RedisFuture<String> setFuture = redisCommands.set("jayjxia","hello");
        LOGGER.debug("【set命令执行结果】{}",setFuture.get());
        RedisFuture<String> getFuture = redisCommands.get("jayjxia");
        LOGGER.debug("【get命令执行结果】{}",getFuture.get());
        statefulRedisConnection.close();//关闭操作
    }
}
