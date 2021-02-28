package com.cj.lottery.dao;

import com.cj.lottery.domain.CjLotteryActivityImg;

import java.util.List;

public interface CjLotteryActivityImgDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CjLotteryActivityImg record);

    int insertSelective(CjLotteryActivityImg record);

    CjLotteryActivityImg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CjLotteryActivityImg record);

    int updateByPrimaryKey(CjLotteryActivityImg record);

    List<CjLotteryActivityImg> listCjLotteryActivityImg(Integer activityId);
}