package com.maker.lock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 主类，启动SpringBoot应用
 * */
@SpringBootApplication
public class StartApplication {
   // RedisAutoConfiguration
   public static void main(String[] args) {
       SpringApplication.run(StartApplication.class,args);
   }
}
