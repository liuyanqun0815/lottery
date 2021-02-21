package com.cj.lottery.dao;

import com.cj.lottery.domain.CjOrderPay;

public interface CjOrderPayDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CjOrderPay record);

    int insertSelective(CjOrderPay record);

    CjOrderPay selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CjOrderPay record);

    int updateByPrimaryKey(CjOrderPay record);
}