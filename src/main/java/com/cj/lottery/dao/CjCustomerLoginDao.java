package com.cj.lottery.dao;

import com.cj.lottery.domain.CjCustomerLogin;

public interface CjCustomerLoginDao {

    int insert(CjCustomerLogin record);

    int insertSelective(CjCustomerLogin record);

    int updateByPrimaryKeySelective(CjCustomerLogin record);

    CjCustomerLogin selectById(Integer id);


}