package com.cj.lottery.enums;

import lombok.Getter;

@Getter
public enum PayStatusEnum {

    NO_PAY(0, "未支付"),

    PAY(1, "已支付"),

    REFUND(2, "已退款"),

    USED(3,"已核销"),


    ;

    private int code;

    private String desc;

    PayStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PayStatusEnum parse(int code) {

        for (PayStatusEnum pay : PayStatusEnum.values()) {
            if (pay.getCode() == code) {
                return pay;
            }
        }
        throw new RuntimeException("PayStatusEnum parse fail,code:" + code);
    }

}
