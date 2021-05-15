package com.cj.lottery.service.impl;

import com.cj.lottery.dao.CjLotteryActivityDao;
import com.cj.lottery.dao.CjMaidianRecordDao;
import com.cj.lottery.domain.CjLotteryActivity;
import com.cj.lottery.domain.common.CjMaidianRecord;
import com.cj.lottery.enums.MaidianTypeEnum;
import com.cj.lottery.service.MaidianService;
import com.cj.lottery.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;


@Service
@Slf4j
public class MaidianServiceImpl implements MaidianService {


    @Autowired
    private CjMaidianRecordDao maidianRecordDao;
    @Autowired
    private CjLotteryActivityDao activityDao;
    @Autowired
    private MerchantService merchantService;



    @Override
    public int save(CjMaidianRecord record) {
        if (!ObjectUtils.isEmpty(record.getChannel())){
            merchantService.saveMerchant(record.getChannel());
        }
        return maidianRecordDao.insertSelective(record);
    }

    @Override
    @Async
    public int saveRecord(MaidianTypeEnum typeEnum, Integer userId, String ipAddr, String activityCode,String channel) {
        CjMaidianRecord record = new CjMaidianRecord();
        if (!ObjectUtils.isEmpty(activityCode)) {
            CjLotteryActivity activity = activityDao.selectActivityByCode(activityCode);
            if (activity != null) {
                record.setActivityId(activity.getId());
            }
        }
        record.setFunctionType(typeEnum.getCode());
        record.setCustomerId(userId);
        record.setIp(ipAddr);
        record.setChannel(channel);
        return save(record);
    }
}
