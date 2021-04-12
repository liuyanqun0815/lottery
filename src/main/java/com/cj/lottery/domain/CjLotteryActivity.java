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
     * 限时标识 true
     */
    private boolean activityDeadlineFlag;
    /**
     * 限时前金额
     */
//    private Integer totalMoney;

    /**
     * 活动头图
     */
    private String activityImg;

    /**
     * 活动状态，0有效，1无效
     */
    private Integer status;


    /**
     * 活动标识
     */
    private Integer activityFlag;

    /**
     * 活动折扣率
     */
    private Integer activityRate;

    /**
     * 消耗人民币（分）
     */
    private Integer consumerMoney;
    //排序
    private Integer sort;



    private static final long serialVersionUID = 1L;
}