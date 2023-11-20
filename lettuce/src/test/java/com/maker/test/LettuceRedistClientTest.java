package com.maker.test;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisCredentialsProvider;
import io.lettuce.core.RedisURI;
import io.lettuce.core.StaticCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 传统的连接Redis的方式
 * */
public class LettuceRedistClientTest {
    private static final String REDIS_HOST="192.168.88.135";//redis地址
    private static final int PORT=6379;//redis端口
    private static final String REDIS_NAME="default";//使用默认用户（管理员）
    private static final String REDIS_PASSWORD="xia123";//密码
    private static final int DATABASE_INDEX=0;//数据库索引
    private static final Logger LOGGER= LoggerFactory.getLogger(LettuceRedistClientTest.class);
    public static void main(String[] args) {
        RedisURI uri=RedisURI.create(REDIS_HOST,PORT);
        RedisCredentialsProvider credentialsProvider=new StaticCredentialsProvider(REDIS_NAME,REDIS_PASSWORD.toCharArray());
        uri.setCredentialsProvider(credentialsProvider);

        RedisClient redisClient=RedisClient.create(uri);
        LOGGER.debug("【Redis客户端实例】client {}",redisClient);
    }
}
