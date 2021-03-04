package com.cj.lottery.service;

import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.LotteryActivityInfoVo;
import com.cj.lottery.domain.view.LotteryData;

import java.util.List;

public interface LuckDrawLotteryService {

    /**
     * 抽奖权限校验：
     * 比如老用户不能在新用户活动里抽奖
     * @param userId
     * @param activityCode
     * @return
     */
    CjResult<LotteryData> checkAuthority(Integer userId, String activityCode);

    /**
     * 新人活动接口
     * @param userId
     * @return
     */
    LotteryActivityInfoVo newPeopleActivities(Integer userId);

}
