package com.cj.lottery.enums;


import lombok.Getter;

@Getter
public enum BuriedPointEnum {


    LUODIYE_COUNT(1, "落地页"),
    LOOK_BOX(2, "看封盒动画"),
    POPUP_WINDOW(3, "弹出计费界面"),
    CLICK_PAY(4, "点击计费按钮");



    private int code;

    private String desc;

    BuriedPointEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static BuriedPointEnum parse(int code) {

        for (BuriedPointEnum pay : BuriedPointEnum.values()) {
            if (pay.getCode() == code) {
                return pay;
            }
        }
        throw new RuntimeException("BuriedPointEnum parse fail,code:" + code);
    }
}
