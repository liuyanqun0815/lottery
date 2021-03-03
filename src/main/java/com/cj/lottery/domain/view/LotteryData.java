package com.cj.lottery.domain.view;

import io.swagger.annotations.ApiModelProperty;

public class LotteryData {

    @ApiModelProperty("订单号")
    private String outTradeNo;

    /**
     * 回收率，百分制，70
     */
    @ApiModelProperty("回收率，百分制，70")
    private Integer callbackRate;
}
