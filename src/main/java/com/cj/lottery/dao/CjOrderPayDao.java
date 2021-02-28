package com.cj.lottery.dao;

import com.cj.lottery.domain.CjOrderPay;

import java.util.List;

public interface CjOrderPayDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CjOrderPay record);

    int insertSelective(CjOrderPay record);

    CjOrderPay selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CjOrderPay record);

    int updateByPrimaryKey(CjOrderPay record);

    //todo 分页
    List<CjOrderPay> selectByUserId(Integer userId);

    int countByUserId(Integer userId);
}