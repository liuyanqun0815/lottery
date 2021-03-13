package com.cj.lottery.aop;

import com.cj.lottery.constant.ContextCons;
import com.cj.lottery.service.CustomerLoginService;
import com.cj.lottery.util.ContextUtils;
import com.cj.lottery.util.UuidUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 日志拦截器
 *
 * @author liuyanqun
 */
@Component
@Slf4j
public class TraceIdInterceptor implements HandlerInterceptor {

    @Autowired
    private CustomerLoginService customerLoginService;
    @Autowired
    private LoginInterceptor loginInterceptor;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String traceId = UuidUtils.getTraceUUid();
        MDC.put(ContextCons.TRACE_ID, traceId);
        ContextUtils.setTraceId(traceId);
        String token = loginInterceptor.getToken(request);
        if (ObjectUtils.isEmpty(token)){
            log.info("request url:{} ,traceId:{}",request.getRequestURI(),traceId);
            return true;
        }
        Integer userIdByToken = customerLoginService.getUserIdByToken(token);
        log.info("request,userId:{}, url:{} ,traceId:{}",userIdByToken,request.getRequestURI(),traceId);
        if (ObjectUtils.isEmpty(userIdByToken)){
            return true;
        }
        ContextUtils.setUserId(userIdByToken);
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        //调用结束后删除
        MDC.remove(ContextCons.TRACE_ID);
    }

}
