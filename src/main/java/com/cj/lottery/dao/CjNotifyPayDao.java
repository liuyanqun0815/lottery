package com.cj.lottery.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cj.lottery.domain.CjNotifyPay;
import com.cj.lottery.domain.manage.UserPayRecordVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CjNotifyPayDao {

    int insertSelective(CjNotifyPay record);


    int updateByPrimaryKeySelective(CjNotifyPay record);

    int updateStatusByOutTradeNo(@Param("outTradeNo") String outTradeNo, @Param("status") int status);

    List<CjNotifyPay> selectSuccessByUserId(int userId);

    int selectPayCountByCustmerId(int customerId);

    IPage<UserPayRecordVo> selectPayRecord(Page<?> page,
                                           @Param("account") String account,
                                           @Param("customerCode") String customerCode,
                                           @Param("startTime")String startTime,
                                           @Param("endTime")String endTime,
                                           @Param("channel")String channel,
                                           @Param("status") Integer status );
}