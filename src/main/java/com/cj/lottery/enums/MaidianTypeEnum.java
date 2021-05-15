package com.cj.lottery.enums;

import lombok.Getter;

@Getter
public enum MaidianTypeEnum {

    SHOU_YE(1,"首页"),
    ACTIVITY_INFO(2,"活动详情"),
    PAY_BUTTON(3,"活动详情支付按钮")

    ;

    private int code;

    private String desc;

    MaidianTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static MaidianTypeEnum parse(int code) {

        for (MaidianTypeEnum pay : MaidianTypeEnum.values()) {
            if (pay.getCode() == code) {
                return pay;
            }
        }
        throw new RuntimeException("MaidianTypeEnum parse fail,code:" + code);
    }

}
