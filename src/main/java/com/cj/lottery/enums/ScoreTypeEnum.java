package com.cj.lottery.enums;

import lombok.Getter;

@Getter
public enum ScoreTypeEnum {

    ADD(0, "加法"),

    JIAN(1, "减法"),

    ;

    private int code;

    private String desc;

    ScoreTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ScoreTypeEnum parse(int code) {

        for (ScoreTypeEnum pay : ScoreTypeEnum.values()) {
            if (pay.getCode() == code) {
                return pay;
            }
        }
        throw new RuntimeException("ScoreTypeEnum parse fail,code:" + code);
    }

}
