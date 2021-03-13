package com.cj.lottery.enums;


import lombok.Getter;

@Getter
public enum RefundStatusEnum {


    SUCCESS(1,"退款成功"),

    CLOSED(2,"退款关闭"),

    PROCESSING(3,"退款处理中"),

    ABNORMAL(4,"退款异常"),

    ;
    private int code;

    private String desc;

    RefundStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static RefundStatusEnum parse(int code) {

        for (RefundStatusEnum pay : RefundStatusEnum.values()) {
            if (pay.getCode() == code) {
                return pay;
            }
        }
        throw new RuntimeException("RefundStatusEnum parse fail,code:" + code);
    }
}
