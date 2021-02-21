package com.cj.lottery.dao;

import com.cj.lottery.domain.CjLotteryRecord;

public interface CjLotteryRecordDao {

    int insertSelective(CjLotteryRecord record);

    int updateByPrimaryKeySelective(CjLotteryRecord record);
}