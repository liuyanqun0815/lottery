package com.cj.lottery.service;

import com.cj.lottery.domain.simple.CjSimpleLotteryActivity;
import com.cj.lottery.domain.simple.CjSimpleOrderPay;
import com.cj.lottery.domain.simple.CjSimpleProductInfo;

import java.util.List;

public interface SimpleActivityService {


    List<CjSimpleLotteryActivity> queryActivityList();

    List<CjSimpleProductInfo> queryProductByActivityIds(List<Integer> activityIdList);


    CjSimpleOrderPay queryOrderPay(String outTradeNo);

    CjSimpleLotteryActivity queryActivityByCode(String activityCode);

    void sendPrize( String name, String mobile, String address, String addressInfo, String channel,String outTradeNo, Integer productId);

    CjSimpleProductInfo queryProductByCode(String productCode);

    void buriedPoints(Integer id, int code, String ip, String channel, String ua);

    void paySuccess(String outTradeNo);
}
