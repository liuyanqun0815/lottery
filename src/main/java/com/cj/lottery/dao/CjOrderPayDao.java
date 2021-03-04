package com.cj.lottery.dao;

import com.cj.lottery.domain.CjOrderPay;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CjOrderPayDao {

    int insertSelective(CjOrderPay record);


    int updateByPrimaryKeySelective(CjOrderPay record);

    //todo 分页
    List<CjOrderPay> selectByUserId(Integer userId);

    List<CjOrderPay> selectPaySuccessByUserId(Integer userId);


    int countByUserId(Integer userId);

    CjOrderPay selectLatestOrder(@Param("customerId") int customerId,
                                 @Param("startTime")String startTime);

    CjOrderPay selectByUserIdAndOutTradeNo(@Param("customerId") int customerId,
                                           @Param("outTradeNo")String outTradeNo);

}