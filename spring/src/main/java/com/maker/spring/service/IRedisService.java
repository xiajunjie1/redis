package com.maker.spring.service;

import java.util.Set;

public interface IRedisService {
    public boolean add(String key,String value);//【主】数据的写入
    public Set<String> search(String pattern);//【从-A】根据匹配查询KEY
    public String get(String key);//【从-B】实现数据的查询

}
