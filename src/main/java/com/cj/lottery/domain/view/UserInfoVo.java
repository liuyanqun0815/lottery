package com.cj.lottery.domain.view;


import com.cj.lottery.domain.CjCustomerInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserInfoVo {

    /**
     * 用户名称
     */
    @ApiModelProperty("用户名称")
    private String customerName;
    /**
     * 头像地址
     */
    @ApiModelProperty("头像地址")
    private String headUrl;
    /**
     * 扭扭币数量
     */
    @ApiModelProperty("扭扭币数量")
    private Integer niuniuNum;


    public static UserInfoVo doToVo(CjCustomerInfo info){
        UserInfoVo vo = new UserInfoVo();
        vo.setCustomerName(info.getCustomerName());
        vo.setHeadUrl(info.getHeadUrl());
        vo.setNiuniuNum(info.getNiuniuNum());
        return vo;
    }

}
