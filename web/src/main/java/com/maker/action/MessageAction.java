package com.maker.action;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/message")
public class MessageAction {
    @Value("${spring.profiles.active}")
    private String profile;// 运行端口的配置名称
    @Value("${server.port}")
    private int port;//运行端口
    @RequestMapping("/set_session")
    public Object setSession(HttpServletRequest request){
        Map<String,Object> result=new HashMap<>();
        result.put("profile",profile);
        result.put("port",port);
        result.put("sesssionId",request.getSession().getId());
        result.put("message","设置session属性，属性名为xia");
        request.getSession().setAttribute("xia","xia-session");
        return result;
    }
    @RequestMapping("/get_session")
    public Object getSession(HttpServletRequest request){
        Map<String,Object> result=new HashMap<>();
        result.put("profile",profile);
        result.put("port",port);
        result.put("sesssionId",request.getSession().getId());
        result.put("message","获取session属性，属性名为xia");
        result.put("xia",request.getSession().getAttribute("xia"));
        return result;
    }
}
