package com.maker.test;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisCredentialsProvider;
import io.lettuce.core.RedisURI;
import io.lettuce.core.StaticCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lettuce提供的新的操作
 * */
public class LettuceRedistClientByStringTest {
   //也可以替换成redis-socket
    private static final String REDIS_STRING="redis://default:xia123@192.168.88.135:6379/0";//redis连接字符串

    private static final Logger LOGGER= LoggerFactory.getLogger(LettuceRedistClientByStringTest.class);
    public static void main(String[] args) {
        RedisURI uri=RedisURI.create(REDIS_STRING);

        RedisClient redisClient=RedisClient.create(uri);
        LOGGER.debug("【Redis客户端实例】client {}",redisClient);
    }
}
