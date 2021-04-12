package com.cj.lottery.enums;

import lombok.Getter;

@Getter
public enum ImgTypeEnum {


    LUN_BO(1, "轮播图类型"),
    IMG_BODY(2, "详情图类型");


    private int code;

    private String desc;

    ImgTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ImgTypeEnum parse(int code) {

        for (ImgTypeEnum pay : ImgTypeEnum.values()) {
            if (pay.getCode() == code) {
                return pay;
            }
        }
        throw new RuntimeException("ImgTypeEnum parse fail,code:" + code);
    }

}
