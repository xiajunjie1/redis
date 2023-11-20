package com.maker.spring.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
@PropertySource("classpath:config/redis_cluster.properties")
public class RedisClusterConfig {
    @Bean("redisClusterConfiguration")
    public RedisClusterConfiguration redisClusterConfiguration(
            @Value("${redis.cluster.nodes}")
            String nodes,
            @Value("${redis.cluster.max.redirect}")
            int maxRedirect,
            @Value("${redis.username}")
            String username,
            @Value("${redis.password}")
            String password
                                                               ){

        Set<String> nodeSet=new HashSet<>();
        nodeSet.addAll(Arrays.asList(nodes.split(",")));
        RedisClusterConfiguration redisClusterConfiguration=new RedisClusterConfiguration(nodeSet);
        redisClusterConfiguration.setUsername(username);
        redisClusterConfiguration.setPassword(password);
        redisClusterConfiguration.setMaxRedirects(maxRedirect);
        return redisClusterConfiguration;
    }

    @Bean("clusterGenericObjectPoolConfig")
    public GenericObjectPoolConfig clusterGenericObjectPoolConfig(
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
    @Bean("clusterPoolingClientConfiguration")
    public LettucePoolingClientConfiguration clusterPoolingClientConfiguration(
            @Autowired
            @Qualifier("clusterGenericObjectPoolConfig")
            GenericObjectPoolConfig clusterGenericObjectPoolConfig
    ){
        return LettucePoolingClientConfiguration.builder()
                .poolConfig(clusterGenericObjectPoolConfig).build();
    }
    @Bean("clusterConnectionFactory")
    public LettuceConnectionFactory clusterConnectionFactory(
            @Autowired
            @Qualifier("redisClusterConfiguration")
            RedisClusterConfiguration redisClusterConfiguration,
            @Autowired
            @Qualifier("clusterPoolingClientConfiguration")
            LettucePoolingClientConfiguration clusterPoolingClientConfiguration){
            LettuceConnectionFactory lettuceConnectionFactory=new LettuceConnectionFactory(
                    redisClusterConfiguration,clusterPoolingClientConfiguration);
            return lettuceConnectionFactory;
    }
}
