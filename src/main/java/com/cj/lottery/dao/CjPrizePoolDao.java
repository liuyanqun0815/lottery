package com.cj.lottery.dao;

import com.cj.lottery.domain.CjPrizePool;
import com.cj.lottery.domain.CjProductInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CjPrizePoolDao {


    int insertSelective(CjPrizePool record);

    int updateByPrimaryKeySelective(CjPrizePool record);

    List<CjPrizePool> selectProductByActivityId(int activityId );

    int subtractionProductNum(@Param("id") Integer id, @Param("version") String version);
}