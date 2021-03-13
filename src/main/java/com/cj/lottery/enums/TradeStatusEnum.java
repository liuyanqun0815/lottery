package com.cj.lottery.enums;


import lombok.Getter;

@Getter
public enum  TradeStatusEnum {

    SUCCESS(1,"SUCCESS"),

    REFUND(2,"转入退款"),

    NOTPAY(3,"未支付"),

    CLOSED(4,"已关闭"),

    REVOKED(5,"已撤销（付款码支付）"),

    USERPAYING(6,"用户支付中（付款码支付）"),

    PAYERROR(7,"支付失败(其他原因，如银行返回失败)"),


    ;
    private int code;

    private String desc;

    TradeStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TradeStatusEnum parse(int code) {

        for (TradeStatusEnum pay : TradeStatusEnum.values()) {
            if (pay.getCode() == code) {
                return pay;
            }
        }
        throw new RuntimeException("TradeStatusEnum parse fail,code:" + code);
    }


}
