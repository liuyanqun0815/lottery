package com.cj.lottery.service;

import com.cj.lottery.domain.CjPayNiuniuRecord;

import java.util.List;

public interface PayNiuniuRecordService {

    List<CjPayNiuniuRecord> queryPayNiuniuRecordByConsumerId(int consumerId);
}
