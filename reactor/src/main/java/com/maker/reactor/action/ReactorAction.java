package com.maker.reactor.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 用于测试响应式插入redis数据和获取redis数据
 * */
@Controller
@RequestMapping("/reactor/*")
public class ReactorAction {
    @Autowired
    private ReactiveRedisTemplate<String,String> reactiveRedisTemplate;
    @RequestMapping("set_value")
    @ResponseBody
    public Mono<Boolean> setValue(String key,String value){
        return this.reactiveRedisTemplate.opsForValue().set(key,value);
    }
    @RequestMapping("get_value")
    @ResponseBody
    public Mono<String> getValue(String key){
        return this.reactiveRedisTemplate.opsForValue().get(key);
    }
    @RequestMapping("set_hash")
    @ResponseBody
    public Mono<Boolean> setHashValue(String key,String field,String value){
        return this.reactiveRedisTemplate.opsForHash().put(key,field,value);

    }
    @RequestMapping("get_hash")
    @ResponseBody
    public Flux<Map.Entry<Object,Object>> getHashAll(String key){
        return this.reactiveRedisTemplate.opsForHash().entries(key);
    }

}
