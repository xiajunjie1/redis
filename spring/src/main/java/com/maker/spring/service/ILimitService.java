package com.maker.spring.service;

import com.maker.spring.annotation.AccessLimit;

public interface ILimitService {
    @AccessLimit(limit = 5,module = "limit")//进行限流配置
    public String get();
}
