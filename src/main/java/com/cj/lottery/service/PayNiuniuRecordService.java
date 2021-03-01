package com.cj.lottery.service;

import com.cj.lottery.domain.CjPayScoreRecord;

import java.util.List;

public interface PayNiuniuRecordService {

    List<CjPayScoreRecord> queryPayNiuniuRecordByConsumerId(int consumerId);
}
