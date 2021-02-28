package com.cj.lottery.domain.pay;

import lombok.Data;

/**
 * 微信公众号支付请求参数
 */
@Data
public class WxGzhOrderParam {


    /**
     * 应用ID
     */
    private String appid;

    /**
     * 直连商户号
     */
    private String mchid;

    /**
     * 商品描述
     */
    private String description;
    /**
     * 商户订单号
     */
    private String out_trade_no;

    /**
     * 通知地址
     */
    private String notify_url;
    /**
     * 订单金额
     */
    private PayAmount amount;

    private Payer payer;


}
