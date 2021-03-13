package com.cj.lottery.enums;

import lombok.Getter;

@Getter
public enum RefundChannelEnum {

    ORIGINAL(1,"原路退款"),

    BALANCE(2,"退回到余额"),

    OTHER_BALANCE(3,"原账户异常退到其他余额账户"),

    OTHER_BANKCARD(4,"原银行卡异常退到其他银行卡"),


    ;

    private int code;

    private String desc;

    RefundChannelEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static RefundChannelEnum parse(int code) {

        for (RefundChannelEnum pay : RefundChannelEnum.values()) {
            if (pay.getCode() == code) {
                return pay;
            }
        }
        throw new RuntimeException("RefundChannelEnum parse fail,code:" + code);
    }
}
