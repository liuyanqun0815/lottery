package com.cj.lottery.domain.common;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * cj_maidian_record
 * @author 
 */
@Data
public class CjMaidianRecord implements Serializable {
    private Integer id;

    /**
     * 埋点功能类型，枚举
     */
    private Integer functionType;

    /**
     * 用户id
     */
    private Integer customerId;

    /**
     * ip
     */
    private String ip;

    /**
     * 活动id
     */
    private Integer activityId;

    private String channel;

    /**
     * 默认0
     */
    private Byte isDelete;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}