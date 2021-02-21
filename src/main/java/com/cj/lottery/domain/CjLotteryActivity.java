package com.cj.lottery.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * cj_lottery_activity
 * @author 
 */
@Data
public class CjLotteryActivity implements Serializable {
    /**
     * 主键id
     */
    private Integer id;
    /**
     * 活动编码
     */
    private String activityCode;
    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动描述
     */
    private String activityDesc;

    /**
     * 活动限时
     */
    private Date activityDeadline;

    /**
     * 活动头图
     */
    private String activityImg;

    /**
     * 活动状态，0有效，1无效
     */
    private Byte status;



    private static final long serialVersionUID = 1L;
}