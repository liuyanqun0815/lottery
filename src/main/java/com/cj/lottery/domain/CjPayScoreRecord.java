package com.cj.lottery.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * cj_pay_niuniu_record
 * @author 
 */
@Data
@Slf4j
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

    public void setScoreInFen(Float score){

        BigDecimal bigScore = new BigDecimal(String.valueOf(score));
        BigDecimal big100 = new BigDecimal(String.valueOf(100));
        BigDecimal multiply = big100.multiply(bigScore);
        this.score = multiply.toBigInteger().intValue();
        log.info("score:{}",score);

    }
    public String getScoreInFent() {
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