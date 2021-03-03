package com.cj.lottery.enums;

import lombok.Getter;

@Getter
public enum ActivityFlagEnum {

    COMMON(0, "普通活动"),
    NEW_PEPOLE(1, "新人活动"),


    ;
    private int code;

    private String desc;

    ActivityFlagEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ActivityFlagEnum parse(int code) {

        for (ActivityFlagEnum pay : ActivityFlagEnum.values()) {
            if (pay.getCode() == code) {
                return pay;
            }
        }
        throw new RuntimeException("ActivityFlagEnum parse fail,code:" + code);
    }
}
