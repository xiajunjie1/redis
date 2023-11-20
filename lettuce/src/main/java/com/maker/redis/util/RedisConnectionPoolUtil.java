package com.maker.redis.util;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class RedisConnectionPoolUtil {
    private static final String REDIS_STRING="redis://default:xia123@192.168.88.135:6379/0";
    private static final int MAX_IDLE=2;//空闲时，最大维持连接数量
    private static final int MIN_IDLE=2;//空闲时，最小维持连接数量
    private static final int MAX_TOTAL=100;//最大可用连接数
    //定义本次要使用的对象连接池
    private static GenericObjectPool<StatefulRedisConnection<String,String>> pool=null;
    static {
        buildObjectPool();
    }
    private RedisConnectionPoolUtil(){}

    public static RedisClient getRedisClient(){
        RedisURI uri=RedisURI.create(REDIS_STRING);
        RedisClient redisClient=RedisClient.create(uri);//创建redis的客户端
        return redisClient;
    }

    private static void buildObjectPool(){
  //      RedisURI uri=RedisURI.create(REDIS_STRING);
  //      RedisClient redisClient=RedisClient.create(uri);//创建redis的客户端
        GenericObjectPoolConfig poolConfig=new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(MAX_IDLE);
        poolConfig.setMinIdle(MIN_IDLE);
        poolConfig.setMaxTotal(MAX_TOTAL);
        //创建对象连接池
        pool= ConnectionPoolSupport.createGenericObjectPool(()->getRedisClient().connect(),poolConfig);

    }

    public static StatefulRedisConnection<String,String> getConnection() throws Exception{
        return pool.borrowObject();
    }
}
