package com.maker.spring.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
@PropertySource("classpath:config/redis_sentinel.properties")
public class RedisSentinelConfig {
    /**
     * 配置Redis哨兵信息
     * */
    @Bean("redisSentinelConfiguration")
    public RedisSentinelConfiguration redisSentinelConfiguration(
            @Value("${redis.sentinel.nodes}") String nodes,
            @Value("${redis.sentinel.master.name}") String masterName,
            @Value("${redis.username}") String username,
            @Value("${redis.password}") String password,
            @Value("${redis.database}") int database){
        Set<String> sentinelHosts=new HashSet<>();
        //根据,将nodes字符串拆分成数组，并将该数组转为集合添加到set中
        sentinelHosts.addAll(Arrays.asList(nodes.split(",")));
        RedisSentinelConfiguration redisSentinelConfiguration=
                new RedisSentinelConfiguration(masterName,sentinelHosts);

        redisSentinelConfiguration.setUsername(username);
        redisSentinelConfiguration.setPassword(password);
        redisSentinelConfiguration.setDatabase(database);
        return redisSentinelConfiguration;
    }
    //CommonsPool提供，配置连接池的环境
    @Bean("sentinelGenericObjectPoolConfig")
    public GenericObjectPoolConfig sentinelGenericObjectPoolConfig(
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
    @Bean("sentinelClientConfiguration")
    public LettucePoolingClientConfiguration sentinelClientConfiguration(
            @Autowired
            @Qualifier("sentinelGenericObjectPoolConfig")
            GenericObjectPoolConfig sentinelGenericObjectPoolConfig){
        return LettucePoolingClientConfiguration.builder().poolConfig(sentinelGenericObjectPoolConfig).build();
    }

    @Bean("sentinelConnectionFactory")
    public LettuceConnectionFactory sentinelConnectionFactory(
            @Autowired
            @Qualifier("redisSentinelConfiguration")
            RedisSentinelConfiguration redisSentinelConfiguration,
            @Autowired
            @Qualifier("sentinelClientConfiguration")
            LettucePoolingClientConfiguration sentinelClientConfiguration
    ){
        LettuceConnectionFactory connectionFactory=new LettuceConnectionFactory(
                redisSentinelConfiguration,sentinelClientConfiguration
        );
        return connectionFactory;
    }
}
