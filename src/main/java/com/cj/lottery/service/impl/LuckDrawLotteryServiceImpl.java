package com.cj.lottery.service.impl;

import com.cj.lottery.dao.CjLotteryActivityDao;
import com.cj.lottery.dao.CjLotteryRecordDao;
import com.cj.lottery.dao.CjOrderPayDao;
import com.cj.lottery.domain.CjLotteryActivity;
import com.cj.lottery.service.LuckDrawLotteryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    public boolean checkAuthority(Integer userId, String activityCode) {

        CjLotteryActivity activity = cjLotteryActivityDao.selectActivityByCode(activityCode);
        if(null != activity){
            //如果活动不是新用户活动都可以参加
            if(!"1".equals(activity)){
                return true;
            }
            int count = cjLotteryRecordDao.countByConsumerId(userId);
            if(count>0){
                return false;
            }
            return true;
        }
        return false;
    }
}
