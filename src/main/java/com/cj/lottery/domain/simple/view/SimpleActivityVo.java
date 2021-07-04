package com.cj.lottery.domain.simple.view;

import com.cj.lottery.domain.ImgNameVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class SimpleActivityVo implements Serializable {

    @ApiModelProperty("活动唯一标识")
    private String activityCode;

    @ApiModelProperty("活动名称")
    private String activityName;


    @ApiModelProperty("正文url集合")
    List<ImgNameVo> bodyUrlList;

//    @ApiModelProperty("是否限时标识")
//    private boolean limitTime;

    @ApiModelProperty("能够抽中的产品编码")
    private String productCode;

    @ApiModelProperty("消耗扭人民币")
    private int money;

}
