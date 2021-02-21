package com.cj.lottery.util;

import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * @author Roy
 */
public class UuidUtils {

    public static String getUUid() {
        return StringUtils.replace(UUID.randomUUID().toString(), "-", "");
    }

}
