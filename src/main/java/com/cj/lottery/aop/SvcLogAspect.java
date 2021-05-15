package com.cj.lottery.aop;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 日志
 * 拦截器
 * 耗时+响应内容
 * @author liuyanqun
 */
@Aspect
@Component
@Slf4j
public class SvcLogAspect {


    @Pointcut("execution(public * com.cj.lottery.controller.*Controller.*(..))")
    public void methodPointCut() {
    }

    @Around("methodPointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        Long startTime = null;
        Long endTime = null;
        //被代理的类的类名
        String className = pjp.getTarget().getClass().getName();
        //方法名
        String methodName = signature.getName();
        //获取日志
        //参数数组
        Object[] requestParams = pjp.getArgs();

        //接口调用开始响应起始时间
        startTime = System.currentTimeMillis();
        //正常执行方法
        Object response = pjp.proceed();
        //接口调用结束时间
        endTime = System.currentTimeMillis();
        //接口应答之后打印日志
        log.info(String.format("【%s】类的【%s】方法，应答参数：%s", className, methodName, JSON.toJSONString(response)));
        //接口耗时
        log.info(String.format("接口【%s】总耗时(毫秒)：%s", className+"."+methodName, String.valueOf(endTime-startTime)));
        return response;

    }
}