package com.cj.lottery.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cj.lottery.domain.CjCustomerInfo;
import com.cj.lottery.domain.manage.UserBaseInfo;
import lombok.Data;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CjCustomerInfoDao {

    int insertSelective(CjCustomerInfo record);

    int updateByPrimaryKeySelective(CjCustomerInfo record);


    CjCustomerInfo selectById(int id);

    CjCustomerInfo selectByCustomerId(int customerId);

    int selectMaxConsumerId();

    IPage<UserBaseInfo> selectBaseUserInfo(Page<?> page,
                                           @Param("account") String account,
                                           @Param("customerCode") String customerCode,
                                           @Param("startTime")String startTime,
                                           @Param("endTime")String endTime,
                                           @Param("channel")String channel);

}