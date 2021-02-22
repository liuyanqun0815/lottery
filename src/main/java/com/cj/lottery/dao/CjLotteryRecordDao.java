package com.cj.lottery.dao;

import com.cj.lottery.domain.CjLotteryRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CjLotteryRecordDao {

    int insertSelective(CjLotteryRecord record);

    int updateByPrimaryKeySelective(CjLotteryRecord record);

    List<CjLotteryRecord> selectRecordByConsumerIdAndStatus(@Param("status")int status,
                                                            @Param("consumerId")int consumerId);
}