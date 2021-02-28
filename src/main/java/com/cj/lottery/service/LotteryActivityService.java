package com.cj.lottery.service;

import com.cj.lottery.domain.view.LotteryActivityInfoVo;
import com.cj.lottery.domain.view.PageView;

public interface LotteryActivityService {


    /**
     * @param current 当前页
     * @param size 页数
     * @return
     */
    PageView queryActivityListByPage(int current,int size);

//    LotteryActivityInfoVo queryActivityInfo();

    /**
     * 根据活动编号分页查询
     * @param current 当前页
     * @param size 页数
     * @return
     */
    PageView queryActivityDetailsByPage(String activityCode,int current,int size);


}
