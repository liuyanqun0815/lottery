package com.cj.lottery.domain.view;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@Api("货物信息")
public class CjProductInfoVo {

    @ApiModelProperty("商品Id")
    private Integer id;

    @ApiModelProperty("商品编码")
    private String productCode;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("销售价格（分）")
    private Integer salePrice;

    @ApiModelProperty("商品有效期")
    private Date shelfLife;

    @ApiModelProperty("商品描述")
    private String desc;

    @ApiModelProperty("回收率，百分制，70")
    private Integer callbackRate;

    @ApiModelProperty("商品图片url")
    private String productImgUrl;

    @ApiModelProperty("商品标识：0 普通商品 1 隐藏商品")
    private Integer productFlag;
}
