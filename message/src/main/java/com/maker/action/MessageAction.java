package com.maker.action;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/message/*")
public class MessageAction {
    @Value("${spring.profiles.active}")
    private String profile;
    @Value("${server.port}")
    private int port;
    @Value("${message.version}")
    private String version;
    @RequestMapping("get")
    public Object get(){
        Map<String,Object> result=new HashMap<>();
        result.put("profile",this.profile);
        result.put("port",this.port);
        result.put("version",this.version);
        return result;
    }
}
