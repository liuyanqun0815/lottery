package com.cj.lottery.aop;

import com.cj.lottery.constant.ContextCons;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author liuyanqun
 */
@Component
@Slf4j
public class HttpClientTraceIdInterceptor implements HttpRequestInterceptor {


    @Override
    public void process(org.apache.http.HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        String traceId = MDC.get(ContextCons.TRACE_ID);
        //当前线程调用中有traceId，则将该traceId进行透传
        if (traceId != null) {
            //添加请求体
            httpRequest.addHeader(ContextCons.TRACE_ID, traceId);
        }
    }
}