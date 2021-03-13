package com.cj.lottery.dao;

import com.cj.lottery.domain.CjLotteryRecord;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface CjLotteryRecordDao {

    int insertSelective(CjLotteryRecord record);

    int updateByPrimaryKeySelective(CjLotteryRecord record);

    List<CjLotteryRecord> selectRecordByConsumerIdAndStatus(@Param("status")int status,
                                                            @Param("consumerId")int consumerId);

    int countByConsumerId(int consumerId);

    @MapKey("statusKey")
    HashMap<Integer, HashMap<Integer,Long>> getPrizeStatuNum(int custmerId);

    List<CjLotteryRecord> selectNewestRecord();

    List<CjLotteryRecord> selectByIdList(@Param("idList") List<Integer> idList);

    int updateStatusById(@Param("status") int status,@Param("id") Integer id);

    CjLotteryRecord selectByOrderId(Integer orderId);
}