package com.cj.lottery.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * cj_customer_address
 * @author 
 */
@Data
public class CjCustomerAddress implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 用户唯一标识userId
     */
    private Integer customerId;

    /**
     * 省份
     */
    private Integer province;

    /**
     * 城市
     */
    private Integer city;

    /**
     * 地区表中的区ID
     */
    private Integer district;

    /**
     * 具体的地址门牌号
     */
    private String address;

    /**
     * 是否默认 1是，默认0
     */
    private Byte isDefault;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 删除，默认0，1删除
     */
    private Byte isDelete;

    private static final long serialVersionUID = 1L;
}