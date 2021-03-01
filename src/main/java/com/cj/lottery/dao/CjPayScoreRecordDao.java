package com.cj.lottery.dao;

import com.cj.lottery.domain.CjPayScoreRecord;

import java.util.List;

public interface CjPayScoreRecordDao {

    int insertSelective(CjPayScoreRecord record);

    int updateByPrimaryKeySelective(CjPayScoreRecord record);

    List<CjPayScoreRecord> selectByConsumerId(int consumerId);

}