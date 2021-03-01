package com.cj.lottery.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * cj_pay_niuniu_record
 * @author 
 */
@Data
public class CjPayScoreRecord implements Serializable {
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
     * 欧气值
     */
    private Integer score;


    /**
     * 订单状态，1已支付，2已退款
     */
    private Integer status;



    private static final long serialVersionUID = 1L;
}