package com.cj.lottery.event.listener;

import com.alibaba.fastjson.JSON;
import com.cj.lottery.dao.CjCustomerInfoDao;
import com.cj.lottery.domain.CjCustomerInfo;
import com.cj.lottery.enums.ScoreTypeEnum;
import com.cj.lottery.event.model.ScoreEvent;
import com.cj.lottery.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScoreListener {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private CjCustomerInfoDao customerInfoDao;


    @EventListener(classes = ScoreEvent.class)
    public void onApplicationEvent(ScoreEvent event) {
        log.info("ScoreListener event:{}", JSON.toJSONString(event));

        int customerId = event.getCustomerId();
        CjCustomerInfo info = userInfoService.queryUserInfoByCustomerId(customerId);

        ScoreTypeEnum type = event.getType();
        if (ScoreTypeEnum.ADD.equals(type)) {
            CjCustomerInfo userInfo = new CjCustomerInfo();
            userInfo.setId(info.getId());
            userInfo.setScoreInFen(Float.valueOf(event.getScore()) +Float.parseFloat( info.getScoreInFen()));
            customerInfoDao.updateByPrimaryKeySelective(userInfo);
        }
        if (ScoreTypeEnum.JIAN.equals(type)) {
            CjCustomerInfo userInfo = new CjCustomerInfo();
            userInfo.setId(info.getId());
            userInfo.setScoreInFen(Float.valueOf(info.getScore()) - Float.valueOf(event.getScore()));
            customerInfoDao.updateByPrimaryKeySelective(userInfo);
        }

    }
}
