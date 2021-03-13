package com.cj.lottery.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * cj_order_refund
 * @author 
 */
@Data
public class CjOrderRefund implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 用户唯一标识
     */
    private Integer customerId;
    /**
     * 公众账号ID
     */
    private String appid;

    /**
     * 商户号
     */
    private String mchId;
    /**
     * 商户退款单号
     */
    private String outRefundNo;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 微信支付退款号
     */
    private String refundId;

    /**
     * 微信支付订单号
     */
    private String transactionId;

    /**
     * 原订单金额，单位为分
     */
    private Integer totalFee;

    /**
     * 退款金额
     */
    private Integer refund;

    /**
     * 退款渠道 RefundChannelEnum
     */
    private Integer channel;

    /**
     * 退款成功时间
     */
    private Date successTime;

    /**
     * 退款状态  RefundStatusEnum
     */
    private Integer status;

    /**
     * 删除状态，默认0，1删除
     */
    private Integer isDelete;

    /**
     * 退款创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}