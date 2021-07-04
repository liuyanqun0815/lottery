package com.cj.lottery.service;

import com.cj.lottery.domain.CjLotteryActivityImg;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.EnumVo;
import com.cj.lottery.domain.view.LotteryActivityInfoVo;
import com.cj.lottery.domain.view.PageView;

import java.util.List;

public interface LotteryActivityService {


    /**
     * @param current 当前页
     * @param size 页数
     * @return
     */
    PageView queryActivityListByPage(int current,int size,Integer userId);

//    LotteryActivityInfoVo queryActivityInfo();

    /**
     * 根据活动编号分页查询
     * @return
     */
    CjResult<LotteryActivityInfoVo> queryActivityDetailsByPage(int userId,String activityCode);


    CjResult<List<EnumVo>> queryAllActivity();
}
