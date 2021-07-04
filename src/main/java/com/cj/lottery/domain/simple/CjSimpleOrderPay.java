package com.cj.lottery.domain.simple;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * cj_simple_order_pay
 * @author 
 */
@Data
public class CjSimpleOrderPay implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 商品描述
     */
    private String body;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 微信支付宝订单号
     */
    private String transactionId;

    /**
     * 订单总金额，单位为分
     */
    private Integer totalFee;

    /**
     * 交易类型
     */
    private Integer tradeType;

    /**
     * 交易起始时间
     */
    private Date timeStart;

    /**
     * 订单状态，0未支付，1已支付
     */
    private Integer status;

    /**
     * 渠道
     */
    private String channel;



    private static final long serialVersionUID = 1L;
}