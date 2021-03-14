package com.cj.lottery.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * cj_lottery_maopao
 * @author 
 */
@Data
public class CjLotteryMaopao implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 活动id
     */
    private Integer activityId;

    /**
     * 用户信息
     */
    private String customerContent;

    /**
     * 奖品图片
     */
    private String prizeImg;

    /**
     * 数据有效行，1 无效，默认0
     */
    private Integer status;



    private static final long serialVersionUID = 1L;
}