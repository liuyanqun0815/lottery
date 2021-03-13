package com.cj.lottery.dao;

import com.cj.lottery.domain.CjLotteryActivityImg;

import java.util.List;

public interface CjLotteryActivityImgDao {

    int insertSelective(CjLotteryActivityImg record);


    int updateByPrimaryKeySelective(CjLotteryActivityImg record);


    List<CjLotteryActivityImg> listCjLotteryActivityImg(Integer activityId);
}