package com.cj.lottery.dao.simple;

import com.cj.lottery.domain.simple.CjSimpleProductInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CjSimpleProductInfoDao {

    int insertSelective(CjSimpleProductInfo record);


    int updateByPrimaryKeySelective(CjSimpleProductInfo record);

    List<CjSimpleProductInfo> selectProductByActivityIds(@Param("activityIdList") List<Integer> activityIdList);

    CjSimpleProductInfo selectByCode(String productCode);
}