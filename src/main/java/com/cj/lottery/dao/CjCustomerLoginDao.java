package com.cj.lottery.dao;

import com.cj.lottery.domain.CjCustomerInfo;
import com.cj.lottery.domain.CjCustomerLogin;
import org.apache.ibatis.annotations.Param;

public interface CjCustomerLoginDao {

    int insert(CjCustomerLogin record);

    int insertSelective(CjCustomerLogin record);

    int updateByPrimaryKeySelective(CjCustomerLogin record);

    CjCustomerLogin selectById(Integer id);


    CjCustomerLogin selectByLoginPhone(String login);
    CjCustomerInfo selectByAccountAndPassword(@Param("account") String account, @Param("password") String password);

}