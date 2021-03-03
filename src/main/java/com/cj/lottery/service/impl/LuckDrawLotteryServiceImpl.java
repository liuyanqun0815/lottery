package com.cj.lottery.service.impl;

import com.cj.lottery.dao.CjLotteryActivityDao;
import com.cj.lottery.dao.CjLotteryRecordDao;
import com.cj.lottery.dao.CjOrderPayDao;
import com.cj.lottery.domain.CjLotteryActivity;
import com.cj.lottery.domain.CjOrderPay;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.LotteryData;
import com.cj.lottery.enums.ActivityFlagEnum;
import com.cj.lottery.enums.ErrorEnum;
import com.cj.lottery.enums.PayStatusEnum;
import com.cj.lottery.service.LuckDrawLotteryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 *  @Description: 抽奖service
 *  @author: zhao_yd
 *  @Date: 2021/2/28 4:24 下午
 *
 */

@Slf4j
@Service
public class LuckDrawLotteryServiceImpl implements LuckDrawLotteryService {

    @Autowired
    private CjOrderPayDao cjOrderPayDao;
    @Autowired
    private CjLotteryActivityDao cjLotteryActivityDao;
    @Autowired
    private CjLotteryRecordDao cjLotteryRecordDao;

    @Override
    public CjResult<LotteryData> checkAuthority(Integer userId, String activityCode) {

        CjLotteryActivity activity = cjLotteryActivityDao.selectActivityByCode(activityCode);
        if (null == activity){
            return CjResult.fail(ErrorEnum.NOT_ACITVITY);
        }
        //如果活动是新人活动，判断是否是新人
        if (activity.getActivityFlag() == ActivityFlagEnum.NEW_PEPOLE.getCode()){
            List<CjOrderPay> orderPays = cjOrderPayDao.selectByUserId(userId);
            if (!CollectionUtils.isEmpty(orderPays)){
                orderPays = orderPays.stream().filter(s -> s.getStatus() != PayStatusEnum.NO_PAY.getCode()).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(orderPays) && orderPays.size()>1){
                    return CjResult.fail(ErrorEnum.NOT_NEW_PEPOLE);
                }
            }
        }
        if(null != activity){
            //如果活动不是新用户活动都可以参加
            if(!"1".equals(activity)){
                return true;
            }
           return newOrNot(userId);
        }
        return false;
    }

    @Override
    public boolean newOrNot(Integer userId) {
        int count = cjLotteryRecordDao.countByConsumerId(userId);
        if(count>0){
            return false;
        }
        return true;
    }
}
