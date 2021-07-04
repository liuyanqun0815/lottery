package com.cj.lottery.domain.simple;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * cj_simple_customer_info
 * @author 
 */
@Data
public class CjSimpleCustomerInfo implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 渠道
     */
    private String channel;

    /**
     * User-Agent
     */
    private String ua;



    private static final long serialVersionUID = 1L;
}