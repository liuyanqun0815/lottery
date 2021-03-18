package com.cj.lottery.enums;

import lombok.Getter;

@Getter
public enum  ErrorEnum {

    SUCCESS(0,"success"),

    //系统异常
    SYSTEM_ERROR(1001,"系统异常"),
    NullPointer(1002,"空指针异常"),
    SQL_ERROR(1003,"查询异常"),
    PHONE_FORMAT_ERROR(1004,"手机号格式有误"),
    SMS_FREQUENTLY(1005,"发送验证码太频繁"),
    SMS_FAILED(1006,"发送验证码失败"),
    SMS_CODE(1007,"验证码错误"),
    IP_ERROR(1008,"客户端ip获取失败"),

    //业务异常
    PARAM_ERROR(2000,"参数错误"),
    USERINFO_NOT_EXIST(2001,"用户信息不存在"),
    NOT_TOKEN(2002,"未登录"),
    NOT_NEW_PEPOLE(2003,"不是新用户！"),
    NOT_ACITVITY(2004,"活动不存在！"),
    NOT_PRIZE(2005,"不存在奖品"),
    PRIZE_PAY(2006,"奖品不满5件，请先支付运费"),
    PRIZE_BELONG(2007,"奖品内容有误，请确认"),
    PRIZE_STATUS_ERROR(2008,"奖品状态有误"),
    PRIZE_IVALID(2009,"奖品中存在无效奖品"),
    PRIZE_BELONG_ERROR(2010,"奖品归属异常"),

    NOT_ORDER(3001,"不存在订单信息"),
    NO_PAY(3002,"未支付"),
    REFUND(3003,"已退款"),
    USED(3004,"已核销"),
    LOTTER_STATUS(3005,"奖品存在中转状态，请联系客服处理"),
    LOTTER_USERD(3006,"奖品已使用或已回收"),
    RECOVER(3007,"已经回收"),
    ORDER_EXCEPTION(3008,"订单状态异常"),
    PAY_MONEY_ERROR(3009,"支付金额和活动不一致"),


    USER_NOT_PAY(4001,"用户未支付"),
    USER_PAY_NOT_ACITVITY(4002,"用户充值和参与的活动不匹配"),




            ;

    public int code;

    private String desc;

    ErrorEnum(int code,String desc){
        this.code = code;
        this.desc =desc;
    }

}
