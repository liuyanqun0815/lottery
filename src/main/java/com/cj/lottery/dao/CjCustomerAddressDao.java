package com.cj.lottery.dao;

import com.cj.lottery.domain.CjCustomerAddress;

import java.util.List;

public interface CjCustomerAddressDao {
    int insertSelective(CjCustomerAddress record);

    int updateByPrimaryKeySelective(CjCustomerAddress record);

    List<CjCustomerAddress> selectByCustmerId(int custmerId);

    int deleteByPrimaryKey(Integer id);

}