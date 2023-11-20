package com.maker.test;

import com.maker.spring.StartRedisApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ContextConfiguration(classes = {StartRedisApplication.class})
@ExtendWith(SpringExtension.class)
public class RedisPipelineTest {
    private static final Logger LOGGER= LoggerFactory.getLogger(RedisPipelineTest.class);
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Test
    public void pipelineTest(){
        this.redisTemplate.executePipelined(new RedisCallback<Object>() {//回调操作
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                //在使用redisTemplate的时候，原本的RedisConnection都已经被覆盖了
                //但是在进行回调处理的时候，会自动的注入RedisConnection对象的实例，以获取Redis连接

                connection.flushDb();
                connection.openPipeline();//开启流水线
                for(int x=0;x<3;x++){
                    //正常执行一条命令
                    connection.listCommands().lPush("list:xia:2023911".getBytes(),("Hello world-"+x).getBytes());
                }
                    //由于使用了流水线，以上的命令会一次性提交给redis
                   List result= connection.closePipeline();//上面的命令提交后，通过执行该方法获取命令执行的结果
                LOGGER.debug("【命令流水线】{}",result);
                return null;
            }
        });
    }
    @Test
    public void pipelineTestRead(){
        List<String> result=new ArrayList<>();
        this.redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();
                for(int i=0;i<3;i++){
                    connection.listCommands().lPop("list:xia:2023911".getBytes());
                }
                List<Object> tempResult=connection.closePipeline();
                tempResult.forEach((data)->{
                    result.add(new String((byte[]) data));
                });
                return null;
            }

        });
        LOGGER.debug("【流水线获取数据】{}",result);
    }
}
