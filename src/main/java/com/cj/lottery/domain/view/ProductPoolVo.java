package com.cj.lottery.domain.view;

import lombok.Data;

@Data
public class ProductPoolVo {


    private String activityName;

    private String productName;

    private int productNum;

    private int productLatestNum;

    private int price;

    private int status;

    private String createTime;

}
