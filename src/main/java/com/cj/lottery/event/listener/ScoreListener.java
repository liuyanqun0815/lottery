package com.cj.lottery.event.listener;

import com.alibaba.fastjson.JSON;
import com.cj.lottery.dao.CjCustomerInfoDao;
import com.cj.lottery.domain.CjCustomerInfo;
import com.cj.lottery.domain.CjPayScoreRecord;
import com.cj.lottery.enums.ScoreTypeEnum;
import com.cj.lottery.event.model.ScoreEvent;
import com.cj.lottery.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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
            BigDecimal a = new BigDecimal(event.getScore());
            BigDecimal b = new BigDecimal(info.getScoreInFen());
            userInfo.setScoreInFen(a.add(b).toString());
            customerInfoDao.updateByPrimaryKeySelective(userInfo);
        }
        if (ScoreTypeEnum.JIAN.equals(type)) {
            CjCustomerInfo userInfo = new CjCustomerInfo();
            userInfo.setId(info.getId());
            BigDecimal a = new BigDecimal(event.getScore());
            BigDecimal b = new BigDecimal(info.getScoreInFen());
            userInfo.setScoreInFen(b.subtract(a).toString());
            customerInfoDao.updateByPrimaryKeySelective(userInfo);
        }

    }

    public int getScoreInFen(Float score) {
        BigDecimal bigScore = new BigDecimal(String.valueOf(score));
        BigDecimal big100 = new BigDecimal(String.valueOf(100));
        BigDecimal multiply = big100.multiply(bigScore);
        return multiply.toBigInteger().intValue();
    }
}
