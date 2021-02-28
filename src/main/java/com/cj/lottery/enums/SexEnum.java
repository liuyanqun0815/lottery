package com.cj.lottery.enums;

import lombok.Getter;

@Getter
public enum SexEnum {

    BOY(1, "男"),
    GIRL(2, "女");

    private int code;

    private String desc;

    SexEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static SexEnum parse(int code) {

        for (SexEnum pay : SexEnum.values()) {
            if (pay.getCode() == code) {
                return pay;
            }
        }
        throw new RuntimeException("PayStatusEnum parse fail,code:" + code);
    }
}
