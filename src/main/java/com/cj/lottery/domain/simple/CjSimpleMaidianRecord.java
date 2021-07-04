package com.cj.lottery.domain.simple;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * cj_simple_maidian_record
 * @author 
 */
@Data
public class CjSimpleMaidianRecord implements Serializable {
    private Integer id;

    /**
     * 埋点功能类型
     */
    private Integer functionType;

    /**
     * ip
     */
    private String ip;

    /**
     * 活动id
     */
    private Integer activityId;

    /**
     * 渠道
     */
    private String channel;

    /**
     * User-Agent
     */
    private String ua;

    private static final long serialVersionUID = 1L;
}