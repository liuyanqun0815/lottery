package com.cj.lottery.dao;

import com.cj.lottery.domain.CjNotifyPay;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CjNotifyPayDao {

    int insertSelective(CjNotifyPay record);


    int updateByPrimaryKeySelective(CjNotifyPay record);

    int updateStatusByOutTradeNo(@Param("outTradeNo") String outTradeNo, @Param("status") int status);

    List<CjNotifyPay> selectSuccessByUserId(int userId);

    int selectPayCountByCustmerId(int customerId);

}