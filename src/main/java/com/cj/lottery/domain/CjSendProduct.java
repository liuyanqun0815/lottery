package com.cj.lottery.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * cj_send_product
 * @author 
 */
@Data
public class CjSendProduct implements Serializable {
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
     * 收货地址id
     */
    private Integer addressId;

    /**
     * 交易类型
     */
    private Integer tradeType;

    /**
     * 0已发货，1已收货
     */
    private Byte status;

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