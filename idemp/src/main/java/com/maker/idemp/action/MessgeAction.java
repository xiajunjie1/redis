package com.maker.idemp.action;

import com.maker.idemp.annotation.Idempontent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/message/*")
public class MessgeAction {
    @RequestMapping("echo")
    @ResponseBody
    @Idempontent //对该接口进行token验证
    public Object echo (String msg){
        return "【message】"+msg;
    }
}
