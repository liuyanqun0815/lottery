package com.cj.lottery.domain.view;


import lombok.Data;

@Data
public class PaySuccessVo {

    /**
     * 订单号
     */
    private String outTradeNo;

    /**
     * 支付地址
     */
    private String h5_url;
}
