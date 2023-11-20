package com.maker.spring.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@CacheConfig(cacheNames = "xia:message")//定义缓存的名称
public interface IMessageService {
    @Cacheable //当前的数据要进行缓存
    public List<String> findAll();//查询全部数据
}
