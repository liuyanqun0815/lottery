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

    LotteryActivityInfoVo queryActivityInfo();

}
