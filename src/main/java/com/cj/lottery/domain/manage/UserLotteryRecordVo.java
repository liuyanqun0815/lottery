package com.cj.lottery.domain.manage;

import lombok.Data;

@Data
public class UserLotteryRecordVo {

    private String account;
    private String customerCode;
    private String channel;
    private String activityName;

    private int totalFee;
    private String productName;
    private int status;
    private String createTime;
}
