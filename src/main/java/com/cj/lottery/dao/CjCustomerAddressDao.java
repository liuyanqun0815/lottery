package com.cj.lottery.dao;

import com.cj.lottery.domain.CjCustomerAddress;

public interface CjCustomerAddressDao {
    int insertSelective(CjCustomerAddress record);

    int updateByPrimaryKeySelective(CjCustomerAddress record);

}