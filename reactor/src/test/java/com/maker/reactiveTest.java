package com.maker;

import com.maker.reactor.StartReactorApplication;
import com.maker.reactor.action.ReactorAction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * 为了测试方便，直接调用action中的方法
 *
 */

@SpringBootTest(classes = StartReactorApplication.class)
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
public class reactiveTest {
    @Autowired
    private ReactorAction reactorAction;
    private static final Logger LOGGER= LoggerFactory.getLogger(reactiveTest.class);
    @Test
    public void reactiveRedisTest()throws Exception{
        LOGGER.info("【setValue】{}",reactorAction.setValue("reactor:xia","hello").toFuture().get());
        /*reactorAction.getValue("reactor:xia").subscribe((data)->{
            LOGGER.info("【getValue】{}",data);
        });
        LOGGER.info("【setHash】{}",reactorAction.setHashValue("reactor:xia","name","xiajj"));
        LOGGER.info("【setHash】{}",reactorAction.setHashValue("reactor:xia","age","27"));
        Flux<Map.Entry<Object,Object>> flux=reactorAction.getHashAll("reactor:xia");
        flux.subscribe((data)->{
            LOGGER.info("【Hash数据】获取数据内容，名称={}、内容={}",data.getKey(),data.getValue());
        });

*/
    }
    @Test
    public void getValueTest()throws Exception{
        reactorAction.getValue("reactor:xia").subscribe((data)->{
            LOGGER.info("【getValue】{}",data);
        });
    }
    @Test
    public void setHashTest()throws Exception{
        LOGGER.info("【setHash】{}",reactorAction.setHashValue("reactor:hash:xia","name","xiajj").toFuture().get());
        LOGGER.info("【setHash】{}",reactorAction.setHashValue("reactor:hash:xia","age","27").toFuture().get());
    }
    @Test
    public void getHashTest()throws Exception{
        Flux<Map.Entry<Object,Object>> flux=reactorAction.getHashAll("reactor:hash:xia");
        flux.subscribe((data)->{
            LOGGER.info("【Hash数据】获取数据内容，名称={}、内容={}",data.getKey(),data.getValue());
        });
    }
}
