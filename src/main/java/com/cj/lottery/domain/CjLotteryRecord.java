package com.cj.lottery.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * cj_lottery_record
 * @author 
 */
@Data
public class CjLotteryRecord implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 用户唯一标识
     */
    private Integer customerId;

    /**
     * 商品id
     */
    private Integer productId;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 抽奖状态，1 待发货，2已回收，3已发货，默认1
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