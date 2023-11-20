package com.maker.redis.util;

import io.lettuce.core.RedisURI;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.ArrayList;
import java.util.List;

public class RedisClusterConnectionPoolUtil {
    private static final String REDIS_A="redis://xia123@192.168.88.135:6379";
    private static final String REDIS_B="redis://xia123@192.168.88.135:6389";
    private static final String REDIS_C="redis://xia123@192.168.88.135:6399";
    private static final String REDIS_D="redis://xia123@192.168.88.136:6379";
    private static final String REDIS_E="redis://xia123@192.168.88.136:6389";
    private static final String REDIS_F="redis://xia123@192.168.88.136:6399";
    private static final String REDIS_G="redis://xia123@192.168.88.137:6379";
    private static final String REDIS_H="redis://xia123@192.168.88.137:6389";
    private static final String REDIS_I="redis://xia123@192.168.88.137:6399";
    private static final int MAX_IDLE=2;//空闲时，最大维持连接数量
    private static final int MIN_IDLE=2;//空闲时，最小维持连接数量
    private static final int MAX_TOTAL=100;//最大可用连接数
    private static RedisClusterClient redisClient=null;
    private static GenericObjectPool<StatefulRedisClusterConnection<String,String>> pool=null;
    private static ThreadLocal<StatefulRedisClusterConnection<String,String>> connectionThreadLocal=new ThreadLocal<>();

    static {
        buildRedisClient();
        buildRedisPool();
    }

    /**
     * 创建所有Redis节点的RedisURI
     * */
    private static List<RedisURI> buildRedisURI(){
        List<RedisURI> redisURIS=new ArrayList<>();
        redisURIS.add(RedisURI.create(REDIS_A));
        return redisURIS;
    }

    /**
     * 创建RedisClusterClient
     * */
    private static void buildRedisClient(){
        if(redisClient==null){
            redisClient=RedisClusterClient.create(buildRedisURI());
        }
    }
    /**
     * 创建连接池
     * */
    private static void buildRedisPool(){
        GenericObjectPoolConfig poolConfig=new GenericObjectPoolConfig();
        poolConfig.setMinIdle(MIN_IDLE);
        poolConfig.setMaxIdle(MAX_IDLE);
        poolConfig.setMaxTotal(MAX_TOTAL);
        pool= ConnectionPoolSupport.createGenericObjectPool(()->{
            StatefulRedisClusterConnection<String,String> redisClusterConnection=redisClient.connect();
            return redisClusterConnection;
        },poolConfig);
    }
    /**
     * 获取连接
     * */
    public static StatefulRedisClusterConnection<String,String> getRedisClusterConnection(){
        StatefulRedisClusterConnection<String,String> redisClusterConnection=connectionThreadLocal.get();
        if(redisClusterConnection==null){
            try{
            redisClusterConnection= pool.borrowObject();
            connectionThreadLocal.set(redisClusterConnection);
        }catch (Exception e){
                e.printStackTrace();
            }
        }
        return redisClusterConnection;
    }

    public static  void close(){
        StatefulRedisClusterConnection<String,String> connection=connectionThreadLocal.get();
        if(connection!=null){
            connection.close();
            connectionThreadLocal.remove();

        }
    }

}
