package com.cj.lottery.domain.simple;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * cj_simple_lottery_activity
 * @author 
 */
@Data
public class CjSimpleLotteryActivity implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 活动编号
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
     * 活动标识：0 普通活动，1 新人活动
     */
    private Integer activityFlag;

    /**
     * 活动头图
     */
    private String activityImg;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 活动状态，0有效，1无效
     */
    private Integer status;

    /**
     * 限时前的价格
     */
    private Integer money;



    private static final long serialVersionUID = 1L;
}