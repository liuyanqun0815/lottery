package com.cj.lottery.domain;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

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
    @ApiModelProperty("收货姓名")
    private String name;
    @ApiModelProperty("收货手机")
    private String phone;

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
    @ApiModelProperty("收货地址")
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