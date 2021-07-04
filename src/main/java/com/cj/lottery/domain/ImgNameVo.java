package com.cj.lottery.domain;

import com.cj.lottery.domain.simple.CjSimpleProductInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class ImgNameVo {

    @ApiModelProperty("产品图片地址")
    private String imgUrl;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品编码")
    private String productCode;


    public static ImgNameVo ImgVo(CjSimpleProductInfo productInfo){
        ImgNameVo vo = new ImgNameVo();
        String productImgUrl = productInfo.getProductImgUrl();
//        https://cos.keyundz.cn/landing/smart/多用螺丝刀套装.png
        productImgUrl = productImgUrl.replace("/smart/","/big/");
        vo.setImgUrl(productImgUrl);
        vo.setProductName(productInfo.getProductName());
        vo.setProductCode(productInfo.getProductCode());
        return vo;
    }

    public static void main(String[] args) {
        String smg="https://cos.keyundz.cn/landing/smart/多用螺丝刀套装.png";
        String[] split = smg.split("/");
        smg= smg.replace("/smart/","/big/");
        System.out.println(smg);
    }
}
