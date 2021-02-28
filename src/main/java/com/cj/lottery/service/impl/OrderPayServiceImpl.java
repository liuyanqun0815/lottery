package com.cj.lottery.service.impl;

import com.cj.lottery.dao.CjCustomerLoginDao;
import com.cj.lottery.dao.CjOrderPayDao;
import com.cj.lottery.domain.CjCustomerLogin;
import com.cj.lottery.domain.CjOrderPay;
import com.cj.lottery.enums.PayStatusEnum;
import com.cj.lottery.enums.PayTypeEnum;
import com.cj.lottery.service.OrderPayService;
import com.cj.lottery.util.UuidUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class OrderPayServiceImpl implements OrderPayService {

    @Value("${app.weixin.pay.appid}")
    private String appid;
    @Value("${app.weixin.pay.mchid}")
    private String mchid;
    @Value("${app.weixin.pay.notifyUrl}")
    private String notifyUrl;
    @Autowired
    private CjOrderPayDao orderPayDao;
    @Autowired
    private CjCustomerLoginDao cjCustomerLoginDao;

    @Override
    public String createWxOrderPay(int customerId, int totalFee) {
        totalFee = totalFee *100;
        CjCustomerLogin login = cjCustomerLoginDao.selectById(customerId);
        String out_trade_no = UuidUtils.getOrderNo();
        this.buildOrderParam(customerId);
        CjOrderPay cjOrderPay = this.buildOrderPayDO(customerId, totalFee, out_trade_no);
        return null;
    }

    private void buildOrderParam (int customerId){
        CjCustomerLogin login = cjCustomerLoginDao.selectById(customerId);
        String openid = login.getLoginPhone();
        String out_trade_no = UuidUtils.getOrderNo();
        String notify_url = notifyUrl;



    }

    private CjOrderPay buildOrderPayDO (int customerId,int totalFee,String out_trade_no){
        CjOrderPay orderPay = new CjOrderPay();
        String description = "充值扭扭币";
        orderPay.setAppid(appid);
        orderPay.setBody(description);
        orderPay.setCustomerId(customerId);
        orderPay.setMchId(mchid);
        orderPay.setOutTradeNo(out_trade_no);
        orderPay.setStatus(PayStatusEnum.NO_PAY.getCode());
        orderPay.setTradeType(PayTypeEnum.WX_GZH.getCode());
        orderPay.setTotalFee(totalFee);
        return orderPay;
    }
}
