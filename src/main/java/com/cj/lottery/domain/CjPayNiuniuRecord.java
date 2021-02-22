package com.cj.lottery.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * cj_pay_niuniu_record
 * @author 
 */
@Data
public class CjPayNiuniuRecord implements Serializable {
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
     * 订单总金额，单位为分
     */
    private Integer totalFee;

    /**
     * 扭扭币数量
     */
    private Integer niuniuNum;

    /**
     * 人民币兑换比例，3，标识1元对3个扭扭币
     */
    private Integer rate;

    /**
     * 交易失效时间
     */
    private Date timeExpire;

    /**
     * 订单状态，1已支付，2已退款
     */
    private Integer status;

    /**
     * 删除状态，默认0，1删除
     */
    private Integer isDelete;

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