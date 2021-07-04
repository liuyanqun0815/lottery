package com.cj.lottery.domain.simple.view;

import io.swagger.annotations.ApiModelProperty;

public class SimpleProductVo {

    /**
     * 商品编码
     */

    private String productCode;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String productName;

    /**
     * 商品图片url
     */
    @ApiModelProperty("商品图片url")
    private String productImgUrl;
}
