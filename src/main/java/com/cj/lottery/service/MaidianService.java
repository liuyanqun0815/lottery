package com.cj.lottery.service;

import com.cj.lottery.domain.common.CjMaidianRecord;
import com.cj.lottery.enums.MaidianTypeEnum;

public interface MaidianService {


  int  save(CjMaidianRecord record);


    int saveRecord(MaidianTypeEnum shouYe, Integer userId, String ipAddr, String i,String channel);
}
