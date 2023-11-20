package com.maker.lock.task;

import com.maker.lock.util.DistributedLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ResourceTask {
    private static final String BUSINESS_KEY="xia-resource";//定义锁标记
    @Autowired
    public DistributedLockUtil distributedLockUtil;
    @Async //基于Spring实现多线程操作
    public void handle(String userid){
        if(this.distributedLockUtil.lock(BUSINESS_KEY,userid,3L, TimeUnit.SECONDS)){
            //获取到锁资源才能进行资源操作
            log.info("【{}】获取到资源，进行处理操作",Thread.currentThread().getName());
            try{
            TimeUnit.SECONDS.sleep(1L);//模拟业务处理的时间
            }catch (Exception e){}
            log.info("【{}】业务处理完成",Thread.currentThread().getName());
            this.distributedLockUtil.unlock(BUSINESS_KEY,userid);
        }else {
            //未获取到资源
            log.info("【{}】未获取到资源，无法进行处理",Thread.currentThread().getName());
        }
    }
}
