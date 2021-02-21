package com.cj.lottery.domain.view;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LotteryResultVo {
    /**
     * 商品编码
     */
    @ApiModelProperty("商品编码")
    private String productCode;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("奖品url")
    private String productImgUrl;
}
