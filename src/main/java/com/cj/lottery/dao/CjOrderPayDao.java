package com.cj.lottery.dao;

import com.cj.lottery.domain.CjOrderPay;

import java.util.List;

public interface CjOrderPayDao {

    int insertSelective(CjOrderPay record);


    int updateByPrimaryKeySelective(CjOrderPay record);


    int updateByPrimaryKey(CjOrderPay record);

    //todo 分页
    List<CjOrderPay> selectByUserId(Integer userId);

    int countByUserId(Integer userId);

}