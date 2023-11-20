package com.maker.spring.service.impl;

import com.maker.spring.service.ILimitService;
import org.springframework.stereotype.Service;

@Service
public class LimitServiceImpl implements ILimitService {
    @Override
    public String get() {
        return "hello world";
    }
}
