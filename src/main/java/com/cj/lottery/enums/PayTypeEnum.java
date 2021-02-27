package com.cj.lottery.enums;

import lombok.Getter;

@Getter
public enum PayTypeEnum {

    WX_GZH(1, "微信公众号"),

    WX_H5(2, "微信H5"),

    ALI_H5(3, "支付宝H5");


    private int code;

    private String desc;

    PayTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PayTypeEnum parse(int code) {

        for (PayTypeEnum pay : PayTypeEnum.values()) {
            if (pay.getCode() == code) {
                return pay;
            }
        }
        throw new RuntimeException("PayTypeEnum parse fail,code:" + code);
    }
}
