package com.cj.lottery.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cj.lottery.domain.CjLotteryRecord;
import com.cj.lottery.domain.manage.UserLotteryRecordVo;
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

    IPage<UserLotteryRecordVo> selectLotterRecord(Page<?> page,
                                                  @Param("account") String account,
                                                  @Param("customerCode") String customerCode,
                                                  @Param("startTime")String startTime,
                                                  @Param("endTime")String endTime,
                                                  @Param("channel")String channel,
                                                  @Param("status") Integer status);
}