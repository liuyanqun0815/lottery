package com.cj.lottery.dao;

import com.cj.lottery.domain.CjPrizePool;
import com.cj.lottery.domain.CjProductInfo;

import java.util.List;

public interface CjPrizePoolDao {


    int insertSelective(CjPrizePool record);

    int updateByPrimaryKeySelective(CjPrizePool record);

    List<CjPrizePool> selectProductByActivityCode(int activityId );

}