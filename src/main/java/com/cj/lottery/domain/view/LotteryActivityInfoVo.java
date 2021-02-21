package com.cj.lottery.domain.view;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;
@Api("活动详情VO")
public class LotteryActivityInfoVo {

    @ApiModelProperty("轮播图url集合")
    List<String> headUrlList;

    @ApiModelProperty("正文url集合")
    List<String> bodyUrlList;

    @ApiModelProperty("是否限时标识")
    private boolean limitTime;

    @ApiModelProperty("限时倒计时")
    private Date activityDeadline;

    @ApiModelProperty("消耗扭扭币数量")
    private int consumerNum;
}
