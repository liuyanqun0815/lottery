package com.cj.lottery.event.listener;

import com.alibaba.fastjson.JSON;
import com.cj.lottery.dao.CjCustomerInfoDao;
import com.cj.lottery.domain.CjCustomerInfo;
import com.cj.lottery.enums.ScoreTypeEnum;
import com.cj.lottery.event.model.PayEvent;
import com.cj.lottery.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PayListener {

    @Autowired
    private CjCustomerInfoDao customerInfoDao;
    @Autowired
    private UserInfoService userInfoService;

    @EventListener(classes = PayEvent.class)
    public void onApplicationEvent(PayEvent event) {
        log.info("PayListener event:{}", JSON.toJSONString(event));
        int customerId = event.getCustomerId();
        CjCustomerInfo info = userInfoService.queryUserInfoByCustomerId(customerId);

        ScoreTypeEnum type = event.getType();
        if (ScoreTypeEnum.ADD.equals(type)) {
            CjCustomerInfo userInfo = new CjCustomerInfo();
            userInfo.setId(info.getId());
            userInfo.setUserMoney(event.getTotalFee() + info.getUserMoney());
            customerInfoDao.updateByPrimaryKeySelective(userInfo);
        }
        if (ScoreTypeEnum.JIAN.equals(type)) {
            CjCustomerInfo userInfo = new CjCustomerInfo();
            userInfo.setId(info.getId());
            userInfo.setUserMoney(info.getUserMoney() - event.getTotalFee());
            customerInfoDao.updateByPrimaryKeySelective(userInfo);
        }
    }
}
