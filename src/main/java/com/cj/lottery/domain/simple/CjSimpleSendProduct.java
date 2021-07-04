package com.cj.lottery.domain.simple;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * cj_simple_send_product
 * @author 
 */
@Data
public class CjSimpleSendProduct implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 商品id
     */
    private Integer productId;

    private String outTradeNo;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 地址
     */
    private String address;

    /**
     * 地址详情
     */
    private String addressInfo;

    /**
     * 渠道
     */
    private String channel;


    private static final long serialVersionUID = 1L;
}