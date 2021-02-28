package com.cj.lottery.dao;

import com.cj.lottery.domain.CjOrderPay;

public interface CjOrderPayDao {

    int insertSelective(CjOrderPay record);


    int updateByPrimaryKeySelective(CjOrderPay record);

}