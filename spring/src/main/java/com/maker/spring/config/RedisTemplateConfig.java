package com.maker.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisTemplateConfig {

    /**
     * 给SpringCache使用的RedisTemplate，
     * 如果使用了上面进行了JSON序列化配置的template，在读取缓存的时候会报错。
     * */
    @Bean
    public RedisTemplate<String,Object> redisCacheTemplate(@Autowired
                                                               @Qualifier("lettuceConnectionFactory") RedisConnectionFactory connectionFactory){
        RedisTemplate<String,Object> redisTemplate=new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        //追加RedisTemplate序列化操作
        redisTemplate.setKeySerializer(new StringRedisSerializer());//key采用的是字符串的方式管理
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());//值采用jdk的序列化方案
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());//hash key采用字符串的方式管理
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        return redisTemplate;
    }
    @Bean
    public RedisTemplate<String,String> stringRedisTemplate(@Autowired
                                                                @Qualifier("lettuceConnectionFactory") RedisConnectionFactory connectionFactory){
        RedisTemplate<String,String> redisTemplate=new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }
    @Bean
    public StringRedisTemplate stringRedisTemplate2(@Autowired
                                                        @Qualifier("lettuceConnectionFactory") RedisConnectionFactory connectionFactory){
        StringRedisTemplate stringRedisTemplate=new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(connectionFactory);
        return stringRedisTemplate;
    }
}
