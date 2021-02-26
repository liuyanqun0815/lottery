package com.cj.lottery.service.impl;

import com.cj.lottery.dao.CjCustomerLoginDao;
import com.cj.lottery.dao.CjOrderPayDao;
import com.cj.lottery.domain.CjCustomerLogin;
import com.cj.lottery.service.OrderPayService;
import com.cj.lottery.util.UuidUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class OrderPayServiceImpl implements OrderPayService {

    @Value("${app.weixin.pay.appid}")
    private String appid;
    @Value("${app.weixin.pay.mchid}")
    private String mchid;
    @Autowired
    private CjOrderPayDao orderPayDao;
    @Autowired
    private CjCustomerLoginDao cjCustomerLoginDao;

    @Override
    public String createWxOrderPay(int customerId, int totalFee) {
        CjCustomerLogin login = cjCustomerLoginDao.selectById(customerId);
        String openid = login.getLoginPhone();
        String description = "充值扭扭币";
        String out_trade_no = UuidUtils.getOrderNo();
        return null;
    }
}
