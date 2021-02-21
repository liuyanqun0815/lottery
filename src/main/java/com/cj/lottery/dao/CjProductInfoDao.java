package com.cj.lottery.dao;

import com.cj.lottery.domain.CjProductInfo;

public interface CjProductInfoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CjProductInfo record);

    int insertSelective(CjProductInfo record);

    CjProductInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CjProductInfo record);

    int updateByPrimaryKey(CjProductInfo record);
}