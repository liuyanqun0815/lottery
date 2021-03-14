package com.cj.lottery.dao;

import com.cj.lottery.domain.CjLotteryMaopao;

import java.util.List;

public interface CjLotteryMaopaoDao {

    int insertSelective(CjLotteryMaopao record);


    int updateByPrimaryKeySelective(CjLotteryMaopao record);

    List<CjLotteryMaopao> selectByActivityCode(Integer id);

}