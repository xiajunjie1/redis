package com.maker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
/**
 * SpringSession其实就是将session交给了外部的redis进行管理，而不是在像之前一样使用内存进行管理
 * */
@SpringBootApplication
@EnableRedisHttpSession //启用SpringSession
public class StartWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(StartWebApplication.class,args);
    }
}
