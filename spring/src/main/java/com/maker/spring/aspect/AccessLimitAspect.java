package com.maker.spring.aspect;

import com.maker.spring.annotation.AccessLimit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Aspect
@Component
public class AccessLimitAspect {
    private static final String PREFIX="jayjxia";
    private static final String SPLIT=":";
    @Autowired
    private RedisScript<Boolean> limitScript;
    @Autowired
    @Qualifier("stringRedisTemplate2")
    private StringRedisTemplate stringRedisTemplate;
    @Pointcut("execution(public * com.maker..service..*.*(..))")
    private void pointCut(){}
    @Around("pointCut()")
    public Object process(ProceedingJoinPoint joinPoint) throws Throwable{
        //获取当前AOP中拦截的真实业务主题实现类对象
       MethodSignature methodSignature= (MethodSignature)joinPoint.getSignature();
       //获取拦截的方法上的AccessLimit注解
       AccessLimit accessLimit=methodSignature.getMethod().getDeclaredAnnotation(AccessLimit.class);
        if(ObjectUtils.isEmpty(accessLimit)){
            //方法上不存在AccessLimit注解,不限流
            return joinPoint.proceed(); //调用目标主体
        }
        //此时需要调用限流处理，此时拼凑出客户端的key
        String key=PREFIX+SPLIT+accessLimit.module()+"IP";//理论上，该IP地址是动态获取，此处只是模拟有一个ip
        int value = accessLimit.limit();
        System.err.println("【限制值】"+value);
        //发送项目中定义的lua脚本，获取脚本执行结果
        Boolean result=this.stringRedisTemplate.execute(this.limitScript,List.of(key),String.valueOf(value));

        if(!result){
            //达到限流次数，不允许访问
            return null;

        }
        return joinPoint.proceed();//允许调用真实的业务操作
    }


}
