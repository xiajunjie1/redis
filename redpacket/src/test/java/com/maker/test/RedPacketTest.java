package com.maker.test;

import com.maker.StartRedPacketApplication;
import com.maker.service.IRedPacketService;
import com.maker.task.GrabTask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = StartRedPacketApplication.class)
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
public class RedPacketTest {
    @Autowired
    private IRedPacketService redPacketService;
    private static final Logger LOGGER= LoggerFactory.getLogger(RedPacketTest.class);
    @Autowired
    private GrabTask grabTask;
    @Test
    public void splitTest(){
       for(int i=0;i<10;i++){
           List<Integer> allPackets=redPacketService.split(5000,5);
        LOGGER.info("红包拆分结果：{}",allPackets);
       }
    }
    @Test
    public void addTest(){
        List<Integer> packets=redPacketService.split(5000,5);
        String key=redPacketService.add("xiajj",packets);
        LOGGER.info("key值为：{}",key);
    }
    @Test
    public void grabTest() throws Exception{
        for (int i=0;i<10;i++){
            grabTask.grabHandler("redPacket:xiajj1695612497401"
                    ,"jayjxia-"+i);
        }
        TimeUnit.SECONDS.sleep(5);
        LOGGER.info("【抢红包结果】{}",redPacketService.getResult("redPacket:xiajj1695612497401"));
    }

}
