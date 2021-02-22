package com.cj.lottery.service.impl;

import com.cj.lottery.dao.CjPayNiuniuRecordDao;
import com.cj.lottery.domain.CjPayNiuniuRecord;
import com.cj.lottery.service.PayNiuniuRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PayNiuniuRecordServiceImpl implements PayNiuniuRecordService {

    @Autowired
    private CjPayNiuniuRecordDao payNiuniuRecordDao;


    @Override
    public List<CjPayNiuniuRecord> queryPayNiuniuRecordByConsumerId(int consumerId) {

        return payNiuniuRecordDao.selectByConsumerId(consumerId);
    }
}
