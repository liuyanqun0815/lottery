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
    private Integer status;

    /**
     * 商品有效期
     */
    private Date shelfLife;

    /**
     * 版本
     */
    private String version;



    private static final long serialVersionUID = 1L;
}