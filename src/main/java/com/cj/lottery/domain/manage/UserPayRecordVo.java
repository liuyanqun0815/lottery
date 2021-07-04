package com.cj.lottery.domain.manage;

import lombok.Data;

@Data
public class UserPayRecordVo {

    private String account;

    private String customerCode;

    private String channel;

    private String outTradeNo;

//    private int totalFeeInFen;

    private int status;

    private int totalFee;

    private String createTime;


    public void getTotalFee(int totalFeeInFen){
        this.totalFee = totalFeeInFen /100;
    }
}
