package com.cj.lottery.domain.view;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PaySuccessVo {

    /**
     * 订单号
     */
    @ApiModelProperty("订单号")
    private String outTradeNo;

    @ApiModelProperty("随机字符串，订单查询使用")
    private String random;
    /**
     * 支付地址
     */
    @ApiModelProperty("支付地址")
    private String h5_url;
}
