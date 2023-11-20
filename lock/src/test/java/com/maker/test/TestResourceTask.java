package com.maker.test;

import com.maker.lock.StartApplication;
import com.maker.lock.task.ResourceTask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest(classes = StartApplication.class)
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
public class TestResourceTask {
    private static final Logger LOGGER= LoggerFactory.getLogger(TestResourceTask.class);
    @Autowired
    private ResourceTask resourceTask;
    @Test
    public void testTask(){
        for(int i=0;i<3;i++){
            this.resourceTask.handle("xiajj-"+i);//模拟userid
        }
    }
}
