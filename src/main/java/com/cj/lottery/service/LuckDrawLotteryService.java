package com.cj.lottery.service;

import com.cj.lottery.domain.view.*;

import java.util.List;

public interface LuckDrawLotteryService {

    /**
     * 抽奖接口
     * @param userId
     * @param activityCode
     * @return
     */
    CjResult<LotteryData> clickLottery(Integer userId, String activityCode,boolean test);

    /**
     * 新人活动接口
     * @param userId
     * @return
     */
    CjResult<NewPepoleActivityVo> newPeopleActivities(boolean loginFlag, Integer userId);

    /**
     * 根据活动列表随机获取中奖人信息(弹幕)
     * @param activityCode
     * @return
     */
    CjResult<List<CjLotteryMaopaoVo>> getAwardwinningUserInfo(String activityCode);

    CjResult<List<CjLotteryMaopaoVo>> getSimpleAwardwinningUserInfo(String activityCode);
}
