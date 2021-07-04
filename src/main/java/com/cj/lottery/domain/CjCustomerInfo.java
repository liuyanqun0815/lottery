package com.cj.lottery.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * cj_customer_info
 *
 * @author
 */
@Data
public class CjCustomerInfo implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 用户唯一标识，userId
     */
    private Integer customerId;

    /**
     * 用户名称
     */
    private Integer customerName;

    /**
     * 证件类型：1 身份证，2 军官证，3 护照
     */
    private Integer identityCardType;

    /**
     * 证件号码
     */
    private String identityCardNo;

    /**
     * 手机号
     */
    private String mobilePhone;

    /**
     * 邮箱
     */
    private String customerEmail;

    /**
     * 性别，1男，2女
     */
    private Integer sex;

    /**
     * 用户余额单位（分）
     */
    private Integer userMoney;

    //支付次数
    private Integer payCount;

    /**
     * 头像地址
     */
    private String headUrl;
    /**
     * 欧气值
     */
    private Integer score;

    private String channel;

    private String ua;

    private String account;


    public void setScoreInFen(String score) {
        BigDecimal bigScore = new BigDecimal(score);
        BigDecimal big100 = new BigDecimal(String.valueOf(100));
        BigDecimal multiply = big100.multiply(bigScore);
        this.score = multiply.toBigInteger().intValue();
    }

    public String getScoreInFen() {
        if (score % 100 == 0) {
            return (score / 100) + "";
        } else {
            return ((float) score / 100) + "";
        }
    }


    private static final long serialVersionUID = 1L;

}