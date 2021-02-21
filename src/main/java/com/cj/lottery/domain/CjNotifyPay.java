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
    private Integer appid;

    /**
     * 商户号
     */
    private Integer mchId;

    /**
     * 交易类型
     */
    private Integer tradeType;

    /**
     * 订单完成时间
     */
    private Date timeEnd;

    /**
     * 删除状态，默认0，1删除
     */
    private Byte isDelete;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}