package com.cj.lottery.domain.view;


import com.cj.lottery.domain.CjCustomerInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

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
     * 欧气值数量
     */
    @ApiModelProperty("欧气值")
    private String score;

    @ApiModelProperty("用户所获奖品(多个取最新一个)")
    private CjProductInfoVo cjProductInfoVo;

    public static UserInfoVo doToVo(CjCustomerInfo info){
        UserInfoVo vo = new UserInfoVo();
        if (info != null) {
            vo.setCustomerName("ID:" + info.getCustomerName() + "");
            vo.setHeadUrl(info.getHeadUrl());
            vo.setScore(info.getScoreInFen());
        }
        return vo;
    }

}
