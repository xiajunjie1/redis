package com.maker.idemp.action;

import com.maker.idemp.service.ITokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * 获取Token
 * */
@Controller
@RequestMapping("/token/*")
public class TokenAction {
    @Autowired
    private ITokenService tokenService;
    @RequestMapping("get")
    @ResponseBody
    public Object getToken(String code){
        String token=tokenService.createToken(code);
        return token;
    }
}
