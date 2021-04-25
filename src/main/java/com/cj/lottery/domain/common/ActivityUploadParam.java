package com.cj.lottery.domain.common;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class ActivityUploadParam implements java.io.Serializable {

    /**
     * 活动名称
     */
    @Excel(name = "活动名称", width = 25,orderNum="1")
    private String activityName;

    /**
     * 活动限时
     */
    @Excel(name = "活动限时deadline时间", width = 25,orderNum="7",databaseFormat = "YYYY/mm/dd HH:mm:ss")
    private Date activityDeadline;
    /**
     * 限时标识 true
     */
    @Excel(name = "活动是否限时", width = 25,orderNum="5",replace = {"是_1", "否_0"})
    private boolean activityDeadlineFlag;
    /**
     * 限时前金额
     */
//    private Integer totalMoney;

    /**
     * 活动头图
     */
    @Excel(name = "活动图片", width = 25,orderNum="4")
    private String activityImg;


    /**
     * 活动标识
     */
    @Excel(name = "新人活动标识", width = 25,orderNum="2", replace = {"是_1", "否_0"})
    private Integer activityFlag;

    /**
     * 活动折扣率
     */
    @Excel(name = "活动回收折扣百分制", width = 25,orderNum="3")
    private Integer activityRate;

    /**
     * 消耗人民币（分）
     */

    @Excel(name = "活动限时前价格", width = 25,orderNum="6")
    private Integer consumerMoney;
    //排序

    private Integer sort;



    @Excel(name = "活动详情轮播图集合", width = 25,orderNum="7")
    private String lunbo;


}
