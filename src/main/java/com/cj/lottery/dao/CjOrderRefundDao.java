package com.cj.lottery.dao;

import com.cj.lottery.domain.CjOrderRefund;

public interface CjOrderRefundDao {

    int insertSelective(CjOrderRefund record);


    int updateByPrimaryKeySelective(CjOrderRefund record);

}