package com.cj.lottery.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class ImgNameVo {

    @ApiModelProperty("产品图片地址")
    private String imgUrl;

    @ApiModelProperty("产品名称")
    private String productName;
}
