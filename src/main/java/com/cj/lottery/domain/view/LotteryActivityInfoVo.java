package com.cj.lottery.domain.view;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.Date;
import java.util.List;
@Api("活动详情VO")
@Data
public class LotteryActivityInfoVo {

    @ApiModelProperty("活动唯一标识")
    private String activityCode;

    @ApiModelProperty("轮播图url集合")
    List<String> headUrlList;

    @ApiModelProperty("正文url集合")
    List<String> bodyUrlList;

    @ApiModelProperty("是否限时标识")
    private boolean limitTime;

    @ApiModelProperty("欧气值")
    private int score;

    @ApiModelProperty("限时倒计时")
    private Date activityDeadline;

    @ApiModelProperty("消耗扭人民币")
    private int consumerMoney;

    @ApiModelProperty("活动标识:0:普通活动 1:新人活动")
    private Integer activityFlag;

}
