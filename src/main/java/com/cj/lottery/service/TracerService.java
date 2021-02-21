//package com.cj.lottery.service;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.lang.Nullable;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.Resource;
//
//@Service
//@Slf4j
//public class TracerService {
//
//    @Resource
//    private Tracer tracer;
//
//    @Nullable
//    public String getTraceId(){
//        Span currentSpan = tracer.currentSpan();
//        String traceId = null;
//        if (currentSpan != null && null != currentSpan.context()) {
//            traceId = currentSpan.context().traceIdString();
//        }
//        return traceId;
//    }
//
//}