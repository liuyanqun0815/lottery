package com.cj.lottery.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * cj_customer_info
 * @author 
 */
@Data
public class CjCustomerInfo implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 用户唯一标识，userId
     */
    private Integer customerId;

    /**
     * 用户名称
     */
    private String customerName;

    /**
     * 证件类型：1 身份证，2 军官证，3 护照
     */
    private Byte identityCardType;

    /**
     * 证件号码
     */
    private String identityCardNo;

    /**
     * 手机号
     */
    private String mobilePhone;

    /**
     * 邮箱
     */
    private String customerEmail;

    /**
     * 性别，1男，2女
     */
    private Byte sex;

    /**
     * 用户余额单位（分）
     */
    private Integer userMoney;

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