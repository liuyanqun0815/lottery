package com.cj.lottery.service;

import com.cj.lottery.domain.CjOrderPay;
import com.cj.lottery.domain.view.CjResult;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;

public interface OrderPayService {

    CjResult<String> createWxOrderPay(int customerId, int totalFee);

    CjResult<Void> wxOrderNotify();

    CjResult<Void> savePaySuccess(WxPayOrderNotifyResult result);

    /**
     * 查询某个人最近的订单是否支付成
     * @param customerId
     * @return
     */
    CjResult<Boolean> queryLatestOrderStatus(int customerId);

    /**
     * 根据订单号查询订单信息支付成功
     * @param customerId
     * @return
     */
    CjResult<Boolean> queryOrderByUserIdAndOutTradeNo(int customerId,String outTradeNo);


}
