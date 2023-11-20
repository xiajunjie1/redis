package com.maker.lock.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁的工具类
 * */
@Component
@Slf4j
public class DistributedLockUtil {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 设置数据的锁定,当前抢占到线程的资源可以进行锁定
     * key：数据锁的标记
     * userid：操作用户的id，依据该数据可以判断是否重复获取锁
     * timeout：分布式锁的有效时间，涉及到分布式锁失效的问题
     * timeunit：时间单位
     * return : 上锁成功返回true，否则返回false
     *
     * */
    public boolean lock(String key, String userid, long timeout, TimeUnit timeunit){
        String value = userid+":"+System.currentTimeMillis();//要保存的数据，也就是锁本身
        if(stringRedisTemplate.opsForValue().setIfAbsent(key,value,timeout,timeunit)){
            //如果该业务key不存在数据，代表没有上锁，此时可以插入value，对其进行上锁
            //此时代表上锁成功，返回true
            return true;
        }
        String executor=this.stringRedisTemplate.opsForValue().get(key);
        if(StringUtils.hasLength(executor)){
            //锁还未释放
            if(executor.startsWith(userid)){
                //代表该锁被同一个用户再次抢到，此时重新设置锁，将过期时间延长
                this.stringRedisTemplate.opsForValue().set(key,value,timeout,timeunit);
                //该用户再次抢占到资源，加锁成功
                return true;

            }
        }
        //锁还未释放，同时是其它用户所加的锁
        return false;
    }
    /**
     * 解除分布式锁
     * key：锁标记
     * userid：操作用户id
     * */
    public void unlock(String key,String userid){
        String value=this.stringRedisTemplate.opsForValue().get(key);
        log.info("资源操作完毕，释放锁资源{}",value);
        if(StringUtils.hasLength(value) && value.startsWith(userid)){
            log.info("【释放锁资源】");
            //锁存在并且是当前用户设置的锁，此时将其删除
            this.stringRedisTemplate.opsForValue().getOperations().delete(key);
        }
    }

}
