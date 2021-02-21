package com.cj.lottery.dao;

import com.cj.lottery.domain.CjNotifyPay;

public interface CjNotifyPayDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CjNotifyPay record);

    int insertSelective(CjNotifyPay record);

    CjNotifyPay selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CjNotifyPay record);

    int updateByPrimaryKey(CjNotifyPay record);
}