package com.cj.lottery.domain.manage;

import lombok.Data;

import java.util.Date;

@Data
public class UserBaseInfo {

    private String account;

    private String customerCode;

    private int payCount;

    private int userMoney;

    private int score;

    private String createTime;

    private String channel;

}
