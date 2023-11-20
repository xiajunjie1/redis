package com.maker.spring.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;

@Configuration
public class RedisScriptConfig { //lua脚本配置
    @Bean
    public DefaultRedisScript<Boolean> limitScript(){

        DefaultRedisScript<Boolean> script=new DefaultRedisScript<>();
        //Resource就是classpath
        script.setLocation(new ClassPathResource("lua/limit.lua"));//脚本路径
        script.setResultType(Boolean.class);
        return script;
    }

}
