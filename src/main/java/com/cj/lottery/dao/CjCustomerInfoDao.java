package com.cj.lottery.dao;

import com.cj.lottery.domain.CjCustomerInfo;

public interface CjCustomerInfoDao {

    int insertSelective(CjCustomerInfo record);

    int updateByPrimaryKeySelective(CjCustomerInfo record);


    CjCustomerInfo selectById(int id);

    CjCustomerInfo selectByCustomerId(int customerId);

    int selectMaxConsumerId();
}