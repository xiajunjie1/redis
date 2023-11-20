package com.maker.redis.util;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.sentinel.api.StatefulRedisSentinelConnection;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class RedisSentinelConnectionUtils {
    private static final String SENTINEL_ADDRESS="redis-sentinel://xia123@192.168.88.135:26379,"+
            "192.168.88.136:26379,192.168.88.137:26379/#mymaster";//配置哨兵地址
    private static final int MAX_IDLE=2;//空闲时，最大维持连接数量
    private static final int MIN_IDLE=2;//空闲时，最小维持连接数量
    private static final int MAX_TOTAL=100;//最大可用连接数
    private static GenericObjectPool<StatefulRedisSentinelConnection<String,String>> pool=null;

    private static RedisURI buildRedisURI(){ //创建RedisURI
        return RedisURI.create(SENTINEL_ADDRESS);
    }
    static {
        buildObjectPool();
    }
    private RedisSentinelConnectionUtils(){}

    public static RedisClient getRedisClient(){ //创建RedisClient
        return RedisClient.create(buildRedisURI());
    }

    private static void buildObjectPool(){
        GenericObjectPoolConfig poolConfig=new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(MAX_TOTAL);
        poolConfig.setMaxIdle(MAX_IDLE);
        poolConfig.setMinIdle(MIN_IDLE);
        pool= ConnectionPoolSupport.createGenericObjectPool(()->getRedisClient().connectSentinel(),poolConfig);

    }

    public static StatefulRedisSentinelConnection<String,String> getConnection() throws Exception{
        return pool.borrowObject();
    }
}
