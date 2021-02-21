package com.cj.lottery.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * cj_lottery_activity_img
 * @author 
 */
@Data
public class CjLotteryActivityImg implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 活动id
     */
    private Integer activityId;

    /**
     * 图片类型，1活动详情轮播图，2活动详情图
     */
    private Byte type;

    /**
     * 图片排序
     */
    private Integer sore;

    /**
     * 图片地址
     */
    private String imgUrl;

    /**
     * 活动状态，0有效，1无效
     */
    private Byte status;

    /**
     * 删除状态，默认0，1删除
     */
    private Byte isDelete;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}