package com.cj.lottery.dao;

import com.cj.lottery.domain.CjPrizePool;

public interface CjPrizePoolDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CjPrizePool record);

    int insertSelective(CjPrizePool record);

    CjPrizePool selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CjPrizePool record);

    int updateByPrimaryKey(CjPrizePool record);
}