package com.maker.idemp.service.impl;

import com.maker.idemp.service.ITokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
@Service
public class TokenServiceImpl implements ITokenService {
    private static final String TOKEN_PREFIX="xiajjToken";//配置数据的前缀
    private static final long EXPIRE_TIME=30;//设置token过期时间为30s
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public String createToken(String code) {
        String key=TOKEN_PREFIX+code;//生成数据key
        if(this.stringRedisTemplate.hasKey(key)){
            //如果存在该key，表示已有该token还未失效
            return null;
        }
        //如果要生成一个token，可以使用多种方案
        //方案1.雪花算法
        //方案2.基于jwt生成
        String value= UUID.randomUUID().toString();
        //将UUID保存到redis之中，并且设置30s失效
        this.stringRedisTemplate.opsForValue().set(key,value,EXPIRE_TIME, TimeUnit.SECONDS);
        return value;
    }

    @Override
    public boolean checkToken(String code,String token) {
        String key=TOKEN_PREFIX+code;
        String value=this.stringRedisTemplate.opsForValue().get(key);//获取redis中的token
        if(value!=null){
            if(value.equals(token)){
                //传入的token和存储的token相同，验证成功删除存在redis中的token
                this.stringRedisTemplate.delete(key);
                return true;
            }
        }
        //其它情况为验证失败
        return false;
    }
}
