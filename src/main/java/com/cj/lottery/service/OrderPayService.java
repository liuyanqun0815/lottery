package com.cj.lottery.service;

import com.cj.lottery.domain.view.CjResult;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;

public interface OrderPayService {

    CjResult<String> createWxOrderPay(int customerId, int totalFee);

    CjResult<Void> wxOrderNotify();

    CjResult<Void> savePaySuccess(WxPayOrderNotifyResult result);
}
