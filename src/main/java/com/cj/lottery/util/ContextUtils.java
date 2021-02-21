package com.cj.lottery.util;

import com.cj.lottery.constant.ContextCons;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * @author liuyanqun
 */
@Slf4j
public class ContextUtils {

    private static ThreadLocal<HashMap<String, Object>> envStore = new ThreadLocal<HashMap<String, Object>>() {
        @Override
        protected HashMap<String, Object> initialValue() {
            return new HashMap<String, Object>();
        }
    };


    /**
     * 清空线程变量,在程序的AOP中调用
     */
    public static void clear() {
        envStore.get().clear();
    }


    public static void setUserId(int userId) {
        log.debug("ContextUtils:{}", userId);
        envStore.get().put(ContextCons.USER_ID, userId);
    }

    /**
     * 获取用户userid
     * @return
     */
    public static int getUserId() {
        return (int)envStore.get().get(ContextCons.USER_ID);
    }

    public static void setTraceId(String traceId) {
        log.debug("TRACE_ID:{}", traceId);
        envStore.get().put(ContextCons.TRACE_ID, traceId);
    }

    public static String getTraceId() {
        String traceId = (String) envStore.get().get(ContextCons.TRACE_ID);
        return Optional.ofNullable(traceId).orElse("");
    }

}
