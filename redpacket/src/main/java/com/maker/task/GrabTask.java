package com.maker.task;

import com.maker.service.IRedPacketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GrabTask {
    @Autowired
    private IRedPacketService redPacketService;
    @Async
    public void grabHandler(String key,String userid){
        log.info("【{}】用户“{}”抢到的红包金额:{}"
                ,Thread.currentThread().getName(),userid,redPacketService.grab(key,userid));
    }
}
