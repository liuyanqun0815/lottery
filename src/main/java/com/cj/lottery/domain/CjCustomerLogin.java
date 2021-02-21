package com.cj.lottery.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * cj_customer_login
 * @author 
 */
@Data
public class CjCustomerLogin implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 登录名称（登录随机分配，支持修改）
     */
    private String loginPhone;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 登录状态，0有效1无效
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