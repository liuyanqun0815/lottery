package com.cj.lottery.dao.simple;

import com.cj.lottery.domain.simple.CjSimpleCustomerInfo;

public interface CjSimpleCustomerInfoDao {


    int insertSelective(CjSimpleCustomerInfo record);


    int updateByPrimaryKeySelective(CjSimpleCustomerInfo record);

}