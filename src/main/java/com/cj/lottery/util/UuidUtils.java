package com.cj.lottery.util;

import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * @author Roy
 */
public class UuidUtils {

    public static String getUUid() {
        return StringUtils.replace(UUID.randomUUID().toString(), "-", "");
    }

    /**
     * 比较短的uuid
     *
     * @return
     */
    public static String getTraceUUid() {
        return StringUtils.replace(UUID.randomUUID().toString(), "-", "")
                .substring(0, 12);
    }

    /**
     * 生成支付订单号
     *
     * @return
     */
    public static String getOrderNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmssSSS");
        String format = sdf.format(new Date());
        Random rand = new Random();

        format = format + rand.nextInt(100);
        return format;
    }


}
