package com.cj.lottery.domain.view;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LotteryData {

    private Integer id;

    @ApiModelProperty("奖品名称")
    private String productName;

    @ApiModelProperty("订单号")
    private String outTradeNo;

    /**
     * 回收率，百分制，70
     */
    @ApiModelProperty("回收率，百分制，70")
    private Integer callbackRate;

    private String productImgUrl;

    private String score;

}
