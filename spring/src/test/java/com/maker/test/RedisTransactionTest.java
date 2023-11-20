package com.maker.test;

import com.maker.spring.StartRedisApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ContextConfiguration(classes = {StartRedisApplication.class})
@ExtendWith(SpringExtension.class)
public class RedisTransactionTest {
    @Autowired
    private RedisTemplate<String,String> stringRedisTemplate;
    private static final Logger LOGGER= LoggerFactory.getLogger(RedisTransactionTest.class);
    /**
     * RedisTemplate的父接口RedisOperations之中提供了事务的相关操作
     *  执行该代码以后会报错：Error in execution; nested exception is io.lettuce.core.RedisCommandExecutionException: ERR EXEC without MULTI
     *  之前版本使用该方式是可以成功执行的
     *  会出现该问题的原因是因为multi和exec不在一个线程内
     *
     * */
    @Test
    public void transactionTest(){
        this.stringRedisTemplate.getConnectionFactory().getConnection()
                .serverCommands().flushDb();//清空redis数据库
        this.stringRedisTemplate.opsForValue().set("xia:message","jayjxia");
        this.stringRedisTemplate.watch("xia:message");
        this.stringRedisTemplate.multi();
        this.stringRedisTemplate.setEnableTransactionSupport(true);//开启事务支持
        this.stringRedisTemplate.opsForValue().set("xia:message","xiajunjie");
        List<Object> results=this.stringRedisTemplate.exec();
        results.forEach((data)->{
            LOGGER.debug("【事务执行结果】{}",data);
        });

    }
    /**
     * 使用callback的方式，让multi和exec在一个线程之中
     * */
    @Test
    public void transactionTest2(){
        this.stringRedisTemplate.getConnectionFactory().getConnection()
                .serverCommands().flushDb();//清空redis数据库
        this.stringRedisTemplate.opsForValue().set("xia:message","jayjxia");
        List<Object> results=this.stringRedisTemplate.execute(new SessionCallback<List<Object>>() {
            //一个Session处理，并提供回调的机制
            @Override
            public  List<Object> execute(RedisOperations operations) throws DataAccessException {
               operations.watch("xia:message");
               operations.multi();
               operations.opsForValue().set("xia:message","xiajunjie");
                return operations.exec();
            }
        });
        results.forEach((data)->{
            LOGGER.debug("【事务执行结果】{}",data);
        });
    }
}
