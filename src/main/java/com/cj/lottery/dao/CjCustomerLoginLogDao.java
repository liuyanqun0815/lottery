package com.cj.lottery.dao;

import com.cj.lottery.domain.CjCustomerLoginLog;

public interface CjCustomerLoginLogDao {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(CjCustomerLoginLog record);

    Integer selectUserIdByToken(String token);


}