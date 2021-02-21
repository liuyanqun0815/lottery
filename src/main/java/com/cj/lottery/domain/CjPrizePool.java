package com.cj.lottery.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * cj_prize_pool
 * @author 
 */
@Data
public class CjPrizePool implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 商品id
     */
    private Integer productId;

    /**
     * 活动id
     */
    private Integer activityId;

    /**
     * 商品配置数量
     */
    private Integer productNum;

    /**
     * 商品剩余数量
     */
    private Integer productLatestNum;

    /**
     * 奖池中排序
     */
    private Integer sort;

    /**
     * 抽中概率 10( 10%)
     */
    private Integer obtainProbility;

    /**
     * 是否从奖池下架，0否，1是
     */
    private Byte status;

    /**
     * 商品有效期
     */
    private Date shelfLife;

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