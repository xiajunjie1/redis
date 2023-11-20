package com.maker.test;

import com.maker.spring.StartRedisApplication;
import com.maker.spring.vo.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

@ContextConfiguration(classes = {StartRedisApplication.class})
@ExtendWith(SpringExtension.class)
public class RedisTest {
    private static final Logger LOGGER= LoggerFactory.getLogger(RedisTest.class);
    @Autowired
    private LettuceConnectionFactory connectionFactory;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Test
    public void testConnection(){
        RedisConnection redisConnection=connectionFactory.getConnection();
        LOGGER.debug("【获取lettuce的连接】{}",redisConnection);

    }
    @Test
    public void testRedisTemplate(){
        redisTemplate.opsForValue().set("xia:2023911","valueByTemplate");
        LOGGER.debug("【针对普通数据】{}",redisTemplate.opsForValue().get("xia:2023911"));
        redisTemplate.opsForHash().put("xia:hash:2023911","name","xiajunjie");
        redisTemplate.opsForHash().put("xia:hash:2023911","age",27);
        LOGGER.debug("【针对hash数据】名字：{}、年龄：{}",
                redisTemplate.opsForHash().get("xia:hash:2023911","name"),
                redisTemplate.opsForHash().get("xia:hash:2023911","age"));
        redisTemplate.opsForList().leftPush("xia:list:2023911","hello");
        redisTemplate.opsForList().leftPush("xia:list:2023911","nice");
        redisTemplate.opsForList().leftPush("xia:list:2023911","good");
        List<Object> result=redisTemplate.opsForList().range("xia:list:2023911",0,-1);
        LOGGER.debug("【针对list数据】{}",result);
    }
    @Test
    public void saveObject(){
        Book book=new Book("1001","Redis开发实战","李兴华",99.99);
        this.redisTemplate.opsForValue().set("book:redis",book);//向redis中存入对象

    }
    @Test
    public void loadObject(){
       // Book book=(Book) this.redisTemplate.opsForValue().get("book:redis");
        Map book=(Map)this.redisTemplate.opsForValue().get("book:redis");
        LOGGER.debug("【图书信息】bid：{}、title：{}、author：{}、price：{}",
                book.get("bid"),book.get("title"),book.get("author"),book.get("price"));
                //book.getBid(),book.getTitle(),book.getAuthor(),book.getPrice());

    }
    /**
     * 读取JSON数据
     * */
    @Test
    public void loadObject2(){
        Book book=(Book) this.redisTemplate.opsForValue().get("book:redis");
        LOGGER.debug("【图书信息】bid：{}、title：{}、author：{}、price：{}",
                book.getBid(),book.getTitle(),book.getAuthor(),book.getPrice());
    }
}
