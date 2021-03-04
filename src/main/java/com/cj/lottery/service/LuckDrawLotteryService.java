package com.cj.lottery.service;

import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.LotteryData;

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
     * 判断当前用户是否新人
     * @param userId
     * @return
     */
    boolean newOrNot(Integer userId);

}
