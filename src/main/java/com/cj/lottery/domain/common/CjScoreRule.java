package com.cj.lottery.domain.common;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * cj_score_rule
 * @author 
 */
@Data
public class CjScoreRule implements Serializable {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 支付次数
     */
    private String payCount;

    /**
     * 得分
     */
    private String score;

    /**
     * 规则ID
     */
    private Integer ruleId;

    /**
     * 操作符
     */
    private Integer operate;



    private static final long serialVersionUID = 1L;
}