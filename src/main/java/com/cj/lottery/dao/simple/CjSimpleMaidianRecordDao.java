package com.cj.lottery.dao.simple;

import com.cj.lottery.domain.simple.CjSimpleMaidianRecord;

public interface CjSimpleMaidianRecordDao {


    int insertSelective(CjSimpleMaidianRecord record);


    int updateByPrimaryKeySelective(CjSimpleMaidianRecord record);

}