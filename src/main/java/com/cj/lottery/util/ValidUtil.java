package com.cj.lottery.util;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author liuyanqun
 */
public class ValidUtil {

    /**
     * 校验手机号是否合法
     * @param phoneNumber
     * @return
     */
    public static boolean phoneCheck(String phoneNumber) {
        if(StringUtils.isBlank(phoneNumber)) {
            return false;
        }
        if(phoneNumber.length() != 11) {
            return false;
        }
        String regex = "^((13[0-9])|(14[56789])|(15([0-9]))|(16[0-9])|(17[0-8])|(18[0-9])|(19[0-9]))\\d{8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phoneNumber);
        if(m.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 手机号星号脱敏
     * @param phoneNumber
     * @return
     */
    public static String phoneAsteriskEncrypt(String phoneNumber) {
        if(!phoneCheck(phoneNumber)) {
            return "";
        }
        return phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7, 11);
    }
}
