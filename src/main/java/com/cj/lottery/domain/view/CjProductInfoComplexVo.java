package com.cj.lottery.domain.view;

import lombok.Data;

import java.util.Date;


@Data
public class CjProductInfoComplexVo {

    private String productName;


    private String productImgUrl;

    private String activityName;


    private int activityRate;

    private int  price;

    private String createTime;
}
