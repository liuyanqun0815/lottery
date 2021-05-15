package com.cj.lottery.dao;

import com.cj.lottery.domain.common.CjMaidianRecord;

public interface CjMaidianRecordDao {

    int insertSelective(CjMaidianRecord record);

    int updateByPrimaryKeySelective(CjMaidianRecord record);

}