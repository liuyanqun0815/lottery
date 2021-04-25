package com.cj.lottery.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * cj_pay_niuniu_record
 * @author 
 */
@Data
public class CjPayScoreRecord implements Serializable {
    /**
     * 主键id
     */
    private Integer id;


    /**
     * 用户唯一标识
     */
    private Integer customerId;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 欧气值
     */
    private int score;

    public void setScore(String score){
        this.score = (int)(Float.valueOf(score) * 100);
    }
    public String getScore() {
        if (score % 100 == 0) {
            return (score / 100) + "";
        } else {
            return ((float) score / 100) + "";
        }
    }

    /**
     * 加减欧气值，0加，1减
     */
    private int type;



    private static final long serialVersionUID = 1L;
}