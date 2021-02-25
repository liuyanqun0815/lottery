package com.cj.lottery.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * cj_customer_login_log
 * @author 
 */
@Data
public class CjCustomerLoginLog implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * uuid
     */
    private String uniqueCode;

    /**
     * 用户唯一标识userId
     */
    private Integer customerId;

    /**
     * 客户端ip
     */
    private String loginIp;

    /**
     * 登陆状态：0有效，1无效
     */
    private Integer status;


    private static final long serialVersionUID = 1L;
}