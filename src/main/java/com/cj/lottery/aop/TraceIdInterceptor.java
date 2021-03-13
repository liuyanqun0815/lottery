package com.cj.lottery.aop;

import com.alibaba.fastjson.JSON;
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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
        if (ObjectUtils.isEmpty(token)) {
            log.info("request start url:{} ,param:{}", request.getRequestURI(), JSON.toJSONString(request.getParameterMap()));
            return true;
        }
        Integer userIdByToken = customerLoginService.getUserIdByToken(token);
        log.info("request start userId:{}, url:{},param:{}", userIdByToken, request.getRequestURI(), JSON.toJSONString(request.getParameterMap()));
        if (ObjectUtils.isEmpty(userIdByToken)) {
            return true;
        }
        ContextUtils.setUserId(userIdByToken);
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        String content = null;
        log.info("request end ,url:{},request code:{}---{}", request.getRequestURI(), response.getStatus(), content);

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        //调用结束后删除
        MDC.remove(ContextCons.TRACE_ID);
    }

    private void returnJson(HttpServletResponse response, String json) throws Exception {
        ServletOutputStream writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getOutputStream();
            writer.print(json);

        } catch (IOException e) {
            log.error("response error", e);
        } finally {
            if (writer != null)
                writer.close();
        }
    }
}
