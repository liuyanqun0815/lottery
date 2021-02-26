package com.cj.lottery.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * cj_merchant
 * @author 
 */
@Data
public class CjMerchant implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 商户编码
     */
    private String merchantCode;

    /**
     * 公众账号ID
     */
    private Integer appid;

    /**
     * 商户号
     */
    private Integer mchId;

    /**
     * 商户状态，0可用，1不可用
     */
    private Integer status;



    private static final long serialVersionUID = 1L;
}