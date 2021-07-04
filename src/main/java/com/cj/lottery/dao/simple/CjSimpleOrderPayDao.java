package com.cj.lottery.dao.simple;

import com.cj.lottery.domain.simple.CjSimpleOrderPay;
import org.apache.ibatis.annotations.Param;

public interface CjSimpleOrderPayDao {


    int insertSelective(CjSimpleOrderPay record);


    int updateByPrimaryKeySelective(CjSimpleOrderPay record);

    CjSimpleOrderPay selectByOutTradeNo(String outTradeNo);

    int updateOrderStatusByOutradeNo(@Param("status") int status, @Param("outTradeNo") String outTradeNo);
}