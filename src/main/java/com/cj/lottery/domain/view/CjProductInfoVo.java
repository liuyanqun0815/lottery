package com.cj.lottery.domain.view;

import com.cj.lottery.domain.CjLotteryRecord;
import com.cj.lottery.domain.CjProductInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
@Api("奖品信息")
public class CjProductInfoVo {

    @ApiModelProperty("奖品id")
    private Integer id;

//    @ApiModelProperty("商品编码")
//    private String productCode;

    @ApiModelProperty("商品名称")
    private String productName;

    @ApiModelProperty("销售价格（分）")
    private Integer salePrice;

//    @ApiModelProperty("商品有效期")
//    private Date shelfLife;

    @ApiModelProperty("商品描述")
    private String desc;

    @ApiModelProperty("回收率，百分制，70")
    private Integer callbackRate;

    @ApiModelProperty("商品图片url")
    private String productImgUrl;

    @ApiModelProperty("商品标识：0 普通商品 1 隐藏商品")
    private Integer productFlag;


    public static CjProductInfoVo DoToVo(CjLotteryRecord records, Map<Integer, CjProductInfo> productidMap){
        CjProductInfoVo vo = new CjProductInfoVo();
        vo.setId(records.getId());
        if (productidMap != null) {
            CjProductInfo productInfo = productidMap.get(records.getProductId());
            vo.setCallbackRate(productInfo.getCallbackRate());
            vo.setDesc(productInfo.getDesc());
            vo.setProductFlag(productInfo.getProductFlag());
            vo.setProductImgUrl(productInfo.getProductImgUrl());
            vo.setProductName(productInfo.getProductName());
            vo.setSalePrice(productInfo.getSalePrice());
        }
        return vo;
    }
}
