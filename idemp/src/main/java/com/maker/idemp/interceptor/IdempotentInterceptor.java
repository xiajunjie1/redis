package com.maker.idemp.interceptor;

import com.maker.idemp.annotation.Idempontent;
import com.maker.idemp.service.ITokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
@Slf4j
public class IdempotentInterceptor implements HandlerInterceptor {
   @Autowired
    private ITokenService tokenService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("【进入拦截器】");
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod= (HandlerMethod) handler;
        Method method=handlerMethod.getMethod();
        Idempontent idempontent=method.getAnnotation(Idempontent.class);
        if(idempontent != null){
            //方法上添加了Idempontent注解
            return check(request);
        }
        return true;
    }
    /**
     * 验证token
     *  token通过头信息传递
     * */
    private boolean check(HttpServletRequest request){
       String token = request.getHeader("token");
       if(token==null || "".equals("token")){
           //token信息不存在头信息中
           token=request.getParameter("token");
           if(token==null || "".equals("token")){
                log.info("【幂等性检测】检测失败，没有传递token");
                return false;
           }
       }
       //在实际的业务中，需要有一个授权中心，其中code为接入服务的授权码
        //本次不向授权中心申请，模拟一个授权码
       String code=request.getParameter("code");//接收授权码
        if(token!=null && code !=null){
            return this.tokenService.checkToken(code,token);
        }
        return false;
    }
}
