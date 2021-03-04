package com.cj.lottery.enums;


import lombok.Getter;

@Getter
public enum PrizeStatusEnum {

    dai_fa_huo(1, "待发货"),

    yi_hui_shou(2, "已回收"),

    yi_fa_huo(3, "已发货");

    private int code;

    private String desc;

    PrizeStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PrizeStatusEnum parse(int code) {

        for (PrizeStatusEnum pay : PrizeStatusEnum.values()) {
            if (pay.getCode() == code) {
                return pay;
            }
        }
        throw new RuntimeException("PrizeEnum parse fail,code:" + code);
    }

}
