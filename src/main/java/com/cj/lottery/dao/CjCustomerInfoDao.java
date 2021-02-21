package com.cj.lottery.dao;

import com.cj.lottery.domain.CjCustomerInfo;

public interface CjCustomerInfoDao {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(CjCustomerInfo record);

    int updateByPrimaryKeySelective(CjCustomerInfo record);

    int updateByPrimaryKey(CjCustomerInfo record);
}