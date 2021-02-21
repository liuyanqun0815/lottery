package com.cj.lottery.enums;

import lombok.Getter;

@Getter
public enum  ErrorEnum {

    SUCCESS(0,"success"),

    //系统异常
    SYSTEM_ERROR(1001,"系统异常"),
    NullPointer(1002,"空指针异常"),
    SQL_ERROR(1003,"查询异常"),

    //业务异常
    PARAM_ERROR(2000,"参数错误"),

    USERINFO_NOT_EXIST(2001,"用户信息不存在"),
    NOT_TOKEN(2002,"缺少必填参数token"),

            ;

    public int code;

    private String desc;

    ErrorEnum(int code,String desc){
        this.code = code;
        this.desc =desc;
    }

}
