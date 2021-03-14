package com.cj.lottery.domain.view;


import com.cj.lottery.domain.CjLotteryMaopao;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class CjLotteryMaopaoVo {

    /**
     * 用户信息
     */
    private String customerName;

    /**
     * 奖品图片
     */

    @ApiModelProperty("商品图片url")
    private String productImgUrl;


    public static CjLotteryMaopaoVo DoToVo(CjLotteryMaopao maopao){
        CjLotteryMaopaoVo mao = new CjLotteryMaopaoVo();
        mao.setCustomerName(maopao.getCustomerContent());
        mao.setProductImgUrl(maopao.getPrizeImg());

        BeanUtils.copyProperties(maopao,mao);
        return mao;
    }
}
