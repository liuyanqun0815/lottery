package com.cj.lottery.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cj.lottery.constant.ContextCons;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.enums.ErrorEnum;
import com.cj.lottery.service.CustomerLoginService;
import com.cj.lottery.util.ContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.io.PrintWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author liuyanqun
 */
@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {


    @Autowired
    private CustomerLoginService customerLoginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = getToken(request);
        log.info("LoginInterceptor request token:{} param:{}",token, JSON.toJSONString(request.getParameterMap()));
        if (ObjectUtils.isEmpty(token)){
            returnJson(response,JSON.toJSONString(CjResult.fail(ErrorEnum.NOT_TOKEN)));
            return false;
        }
        //token是否存在，获取到userId
        Integer userIdByToken = customerLoginService.getUserIdByToken(token);
        if (userIdByToken == null){
            returnJson(response,JSON.toJSONString(CjResult.fail(ErrorEnum.NOT_TOKEN)));
            return false;
        }
        ContextUtils.setUserId(userIdByToken);
        return true;
    }

    public String getToken(HttpServletRequest request) {
        String token  = request.getHeader(ContextCons.TOKEN);
        if (ObjectUtils.isEmpty(token)){
            token = request.getParameter(ContextCons.TOKEN);
        }
        return token;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

        //调用结束后删除
        MDC.remove(ContextCons.TRACE_ID);
        MDC.remove(ContextCons.USER_ID);
        ContextUtils.clear();
    }

    private void returnJson(HttpServletResponse response, String json) throws Exception{
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);

        } catch (IOException e) {
            log.error("response error",e);
        } finally {
            if (writer != null)
                writer.close();
        }
    }

}
