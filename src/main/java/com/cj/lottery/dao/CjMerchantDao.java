package com.cj.lottery.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cj.lottery.domain.CjMerchant;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CjMerchantDao {

    int updateByPrimaryKeySelective(CjMerchant record);

    List<CjMerchant> selelctAllMerchant();

    int insert(String channel);


    IPage<CjMerchant> selectChannelRecord(Page<CjMerchant> page, @Param("channel") String channel,
                                          @Param("channelName") String channelName);
}