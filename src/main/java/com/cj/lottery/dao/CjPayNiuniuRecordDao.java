package com.cj.lottery.dao;

import com.cj.lottery.domain.CjPayNiuniuRecord;

import java.util.List;

public interface CjPayNiuniuRecordDao {

    int insertSelective(CjPayNiuniuRecord record);

    int updateByPrimaryKeySelective(CjPayNiuniuRecord record);

    List<CjPayNiuniuRecord> selectByConsumerId(int consumerId);

}