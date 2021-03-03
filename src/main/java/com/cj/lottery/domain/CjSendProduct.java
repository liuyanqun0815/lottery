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
     * 是否有邮费，0无1有
     */
    private Integer postageFlag;

    /**
     * 0已发货，1已收货
     */
    private Integer status;



    private static final long serialVersionUID = 1L;
}