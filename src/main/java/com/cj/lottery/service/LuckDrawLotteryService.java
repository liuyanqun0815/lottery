package com.cj.lottery.service;

public interface LuckDrawLotteryService {

    /**
     * 抽奖权限校验：
     * 比如老用户不能在新用户活动里抽奖
     * @param userId
     * @param activityCode
     * @return
     */
    boolean checkAuthority(Integer userId,String activityCode);

}
