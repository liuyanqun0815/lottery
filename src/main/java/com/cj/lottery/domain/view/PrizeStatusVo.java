package com.cj.lottery.domain.view;

import io.swagger.annotations.ApiModelProperty;

public class PrizeStatusVo {

    @ApiModelProperty("待发货数量")
    private int noSendNum;

    @ApiModelProperty("回收数据")
    private int refundNum;

    @ApiModelProperty("已发货数据")
    private int sendNum;
}
