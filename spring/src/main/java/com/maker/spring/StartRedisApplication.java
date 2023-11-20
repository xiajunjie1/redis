package com.maker.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@ComponentScan({"com.maker.spring"})
@EnableAspectJAutoProxy //启用aspectj的代理
public class StartRedisApplication {

}
