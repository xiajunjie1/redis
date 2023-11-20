package com.maker.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
/**
 * SpringCache直接提供了基于redis的Cache与CacheManager两个具体的实现子类
 *
 */

@Configuration
@EnableCaching //开启SpringCache的支持
public class CacheConfig {
    @Bean
    public CacheManager cacheManager(
            @Autowired
            @Qualifier("redisCacheTemplate")
            RedisTemplate<String,Object> redisCacheTemplate){
        RedisCacheWriter redisCacheWriter=RedisCacheWriter
                .nonLockingRedisCacheWriter(redisCacheTemplate.getConnectionFactory());//创建redis的写入缓存
        RedisCacheConfiguration redisCacheConfiguration=RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(redisCacheTemplate.getValueSerializer()));//通过RedisTemplate配置缓存写入Redis的序列化操作
        return new RedisCacheManager(redisCacheWriter,redisCacheConfiguration,"xia:message");//返回Redis的CacheManager实现类，第三个参数为默认的缓存名字
    }
}
