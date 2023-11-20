package com.maker.redis.util;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.ReadFrom;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.masterreplica.MasterReplica;
import io.lettuce.core.masterreplica.StatefulRedisMasterReplicaConnection;
import io.lettuce.core.support.ConnectionPoolSupport;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.ArrayList;
import java.util.List;

public class RedisMasterSlaveClusterConnectionUtil {
    //主节点连接
    private static final String MASTER_REDIS_STRING="redis://default:xia123@192.168.88.135:6379/0";
    //从节点连接
    private static final String SLAVE_A_REDIS_STRING="redis://default:xia123@192.168.88.136:6379/0";
    private static final String SLAVE_B_REDIS_STRING="redis://default:xia123@192.168.88.137:6379/0";

    private static final int MAX_IDLE=2;//空闲时，最大维持连接数量
    private static final int MIN_IDLE=2;//空闲时，最小维持连接数量
    private static final int MAX_TOTAL=100;//最大可用连接数

    private static RedisClient redisClient=null;//表示公共属性
    //定义本次要使用的连接池，连接池中存储的是Redis副本连接接口
    private static GenericObjectPool<StatefulRedisMasterReplicaConnection<String,String>> pool=null;
    private static ThreadLocal<StatefulRedisMasterReplicaConnection> connectionThreadLocal=new ThreadLocal<>();

    static {
            buildRedisClient();//构建RedisClient
            buildObjectPool();

    }
    /**
     * 操作主从架构的关键就是StatefulRedisMasterReplicaConnection连接
     * 该连接是通过Lettuce提供的MasterReplica构建的
     * */
    private static void buildObjectPool(){//构建连接池
        GenericObjectPoolConfig poolConfig=new GenericObjectPoolConfig();
        poolConfig.setMinIdle(MIN_IDLE);
        poolConfig.setMaxIdle(MAX_IDLE);
        poolConfig.setMaxTotal(MAX_TOTAL);
        pool= ConnectionPoolSupport.createGenericObjectPool(()->{
            StatefulRedisMasterReplicaConnection<String,String> redisConnection=
                    //第一个参数是redis客户端，第二个参数是编码类型，第三个参数为一个RedisURI或者是RedisURI的集合
                    MasterReplica.connect(redisClient,new StringCodec(),buildRedisUri());
            //设置读取数据从哪个节点读
            redisConnection.setReadFrom(ReadFrom.REPLICA_PREFERRED);//副本优先读取
                return redisConnection;
        },poolConfig);
    }
    /**
     * 构建RedisClient对象
     * */
    private static void buildRedisClient(){
        if(redisClient==null){
            redisClient=RedisClient.create();


        }
    }
    /**
     * 将主从节点的连接信息构建为RedisURI集合
     * */
    private static List<RedisURI> buildRedisUri(){
        List<RedisURI> redisURIS=new ArrayList<>();
        redisURIS.add(RedisURI.create(MASTER_REDIS_STRING));
        redisURIS.add(RedisURI.create(SLAVE_A_REDIS_STRING));
        redisURIS.add(RedisURI.create(SLAVE_B_REDIS_STRING));
        return redisURIS;
    }
    /**
     * 获取连接对象
     * */
    public static StatefulRedisMasterReplicaConnection getConnection(){
        StatefulRedisMasterReplicaConnection connection= connectionThreadLocal.get();
        if(connection==null){
            try{
                connection = pool.borrowObject();

            }catch (Exception e){
                e.printStackTrace();
            }
            connectionThreadLocal.set(connection);
        }
        return connection;
    }

    public static void close(){
        StatefulRedisMasterReplicaConnection connection=
                connectionThreadLocal.get();
        if(connection != null){
            connection.close();
            connectionThreadLocal.remove();
        }
    }

}
