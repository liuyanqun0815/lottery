package com.cj.lottery.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * cj_notify_pay
 * @author 
 */
@Data
public class CjNotifyPay implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 用户唯一标识
     */
    private Integer customerId;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 微信订单号
     */
    private String transactionId;

    /**
     * 订单总金额，单位为分
     */
    private Integer totalFee;

    /**
     * 公众账号ID
     */
    private String appid;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 交易类型
     */
    private Integer tradeType;

    /**
     * 订单完成时间
     */
    private Date timeEnd;

    /**
     * 订单状态，0未支付，1已支付，2已退款
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}