package com.maker.spring.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

@Configuration
@PropertySource("classpath:config/redis.properties")
public class SpringDataRedisConfig {
    //SpringDataRedis提供，配置redis基本信息
    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfiguration(
            @Value("${redis.host}") String host,
            @Value("${redis.port}") int port,
            @Value("${redis.username}") String username,
            @Value("${redis.password}") String password,
            @Value("${redis.database}") int database){
        RedisStandaloneConfiguration redisStandaloneConfiguration=new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setUsername(username);
        redisStandaloneConfiguration.setPassword(password);
        redisStandaloneConfiguration.setDatabase(database);
        return redisStandaloneConfiguration;
    }
    //CommonsPool提供，配置连接池的环境
    @Bean
    public GenericObjectPoolConfig genericObjectPoolConfig(
            @Value("${redis.pool.maxTotal}") int maxTotal,
            @Value("${redis.pool.maxIdle}") int maxIdle,
            @Value("${redis.pool.minIdle}") int minIdle,
            @Value("${redis.pool.testOnBorrow}") boolean testOnBorrow
            ){
        GenericObjectPoolConfig poolConfig=new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setTestOnBorrow(testOnBorrow);
        return poolConfig;
    }
    //使用的是lettuce相关组件，所以进行lettuce客户端的相关配置处理
    @Bean
    public LettucePoolingClientConfiguration lettucePoolingClientConfiguration(
            @Autowired GenericObjectPoolConfig genericObjectPoolConfig){
        //构建Lettuce连接池
        return LettucePoolingClientConfiguration.builder().poolConfig(genericObjectPoolConfig).build();
    }
    //创建lettuce的连接工厂，此类为SpringDataRedis通过lettuce连接redis最核心的类
    //通过该类，将之前配置的redis连接对象和lettuce连接池对象整合起来
    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory(
            @Autowired RedisStandaloneConfiguration redisStandaloneConfiguration,
            @Autowired LettucePoolingClientConfiguration lettucePoolingClientConfiguration
    ){
        LettuceConnectionFactory connectionFactory=new LettuceConnectionFactory(
                redisStandaloneConfiguration,lettucePoolingClientConfiguration
        );
        return connectionFactory;
    }
}
