package com.cj.lottery.dao;

import com.cj.lottery.domain.CjPrizePool;
import com.cj.lottery.domain.CjProductInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CjProductInfoDao {


    int insertSelective(CjProductInfo record);


    int updateByPrimaryKeySelective(CjProductInfo record);

    CjProductInfo selectById(Integer id);

    List<CjProductInfo> selectByIds(@Param("productIds")List<Integer> productIds);

    List<CjPrizePool> selectPoolPrice();

}