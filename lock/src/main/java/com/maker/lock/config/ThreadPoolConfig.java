package com.maker.lock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync //启动异步支持
public class ThreadPoolConfig { //进行线程池的配置
    @Bean
    public Executor taskExecutor(){
        ThreadPoolTaskScheduler taskScheduler=new ThreadPoolTaskScheduler();//线程池实例
        taskScheduler.setPoolSize(Runtime.getRuntime().availableProcessors()*2);//线程池的数量
        taskScheduler.setThreadNamePrefix("LockThread-");//设置线程名称前缀
        taskScheduler.setAwaitTerminationSeconds(60);//强制等待60s
        taskScheduler.setWaitForTasksToCompleteOnShutdown(true);//等待任务执行完成后在销毁
        taskScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());//设置拒绝策略
        return taskScheduler;
    }
}
