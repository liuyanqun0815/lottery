package com.cj.lottery.service;

import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.LotteryActivityInfoVo;
import com.cj.lottery.domain.view.LotteryData;
import com.cj.lottery.domain.view.NewPepoleActivityVo;

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

}
