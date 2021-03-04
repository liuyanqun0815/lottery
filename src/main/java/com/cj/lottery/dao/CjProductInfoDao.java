package com.cj.lottery.dao;

import com.cj.lottery.domain.CjProductInfo;

import java.util.List;

public interface CjProductInfoDao {


    int insertSelective(CjProductInfo record);


    int updateByPrimaryKeySelective(CjProductInfo record);

    CjProductInfo selectById(Integer id);
}