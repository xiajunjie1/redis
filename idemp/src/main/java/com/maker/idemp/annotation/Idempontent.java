package com.maker.idemp.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * token的检查
 * 当哪些接口（action中的方法）需要进行token检查的时候，就配置上改注解
 * */
@Target(ElementType.METHOD)//定义在方法上
@Retention(RetentionPolicy.RUNTIME)//运行时生效
public @interface Idempontent {
}
