package com.maker.spring.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;

/**
 * Redis主从配置类
 *  一主二从模式，所以需要创建三个Redis实例
 * */
@Configuration
@PropertySource("classpath:config/redis_master_slave.properties")
public class SpringRedisMasterSlaveConfig {
    @Bean("redisMasterConfiguration")
    public RedisStandaloneConfiguration redisMasterStandaloneConfiguration(
            @Value("${redis.master.host}") String host,
            @Value("${redis.master.port}") int port,
            @Value("${redis.username}") String username,
            @Value("${redis.password}") String password,
            @Value("${redis.database}") int database) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setUsername(username);
        redisStandaloneConfiguration.setPassword(password);
        redisStandaloneConfiguration.setDatabase(database);
        return redisStandaloneConfiguration;
    }

    @Bean("redisSlaveAConfiguration")
    public RedisStandaloneConfiguration redisSlaveAStandaloneConfiguration(
            @Value("${redis.slaveA.host}") String host,
            @Value("${redis.slaveA.port}") int port,
            @Value("${redis.username}") String username,
            @Value("${redis.password}") String password,
            @Value("${redis.database}") int database) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setUsername(username);
        redisStandaloneConfiguration.setPassword(password);
        redisStandaloneConfiguration.setDatabase(database);
        return redisStandaloneConfiguration;
    }

    @Bean("redisSlaveBConfiguration")
    public RedisStandaloneConfiguration redisSlaveBStandaloneConfiguration(
            @Value("${redis.slaveB.host}") String host,
            @Value("${redis.slaveB.port}") int port,
            @Value("${redis.username}") String username,
            @Value("${redis.password}") String password,
            @Value("${redis.database}") int database) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setUsername(username);
        redisStandaloneConfiguration.setPassword(password);
        redisStandaloneConfiguration.setDatabase(database);
        return redisStandaloneConfiguration;
    }

    /**
     * 连接池配置信息为公共的配置项
     * */
    @Bean("masterSlaveObjectPoolConfig")
    public GenericObjectPoolConfig masterSlaveObjectPoolConfig(
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
    @Bean("masterSlavePoolingClientConfiguration")
    public LettucePoolingClientConfiguration masterSlavePoolingClientConfiguration(
            @Autowired GenericObjectPoolConfig masterSlaveObjectPoolConfig){
        //构建Lettuce连接池
        return LettucePoolingClientConfiguration.builder().poolConfig(masterSlaveObjectPoolConfig).build();
    }

    /**
     * 配置RedisConnectionFactory
     *  需要配置主节点和两个从节点的连接工厂
     * */

    @Bean("lettuceMasterConnectionFactory")
    public LettuceConnectionFactory lettuceMasterConnectionFactory(
            @Autowired RedisStandaloneConfiguration redisMasterConfiguration,
            @Autowired LettucePoolingClientConfiguration masterSlavePoolingClientConfiguration
    ){
        LettuceConnectionFactory connectionFactory=new LettuceConnectionFactory(
                redisMasterConfiguration,masterSlavePoolingClientConfiguration
        );
        return connectionFactory;
    }
    @Bean("lettuceSlaveAConnectionFactory")
    public LettuceConnectionFactory lettuceSlaveAConnectionFactory(
            @Autowired RedisStandaloneConfiguration redisSlaveAConfiguration,
            @Autowired LettucePoolingClientConfiguration masterSlavePoolingClientConfiguration
    ){
        LettuceConnectionFactory connectionFactory=new LettuceConnectionFactory(
                redisSlaveAConfiguration,masterSlavePoolingClientConfiguration
        );
        return connectionFactory;
    }

    @Bean("lettuceSlaveBConnectionFactory")
    public LettuceConnectionFactory lettuceSlaveBConnectionFactory(
            @Autowired RedisStandaloneConfiguration redisSlaveBConfiguration,
            @Autowired LettucePoolingClientConfiguration masterSlavePoolingClientConfiguration
    ){
        LettuceConnectionFactory connectionFactory=new LettuceConnectionFactory(
                redisSlaveBConfiguration,masterSlavePoolingClientConfiguration
        );
        return connectionFactory;
    }

}
