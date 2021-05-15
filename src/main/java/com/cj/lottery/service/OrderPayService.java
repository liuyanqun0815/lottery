package com.cj.lottery.service;

import cn.felord.payment.wechat.v3.model.RefundConsumeData;
import cn.felord.payment.wechat.v3.model.TransactionConsumeData;
import com.cj.lottery.domain.CjLotteryActivity;
import com.cj.lottery.domain.CjOrderPay;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.PaySuccessVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface OrderPayService {

    CjResult<String> createWxOrderPay(int customerId, int totalFee,String activityCode);

//    CjResult<Void> wxOrderNotify();

//    CjResult<Void> savePaySuccess(WxPayOrderNotifyResult result);

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


    CjResult<PaySuccessVo> createWxH5OrderPay(int userId, int totalFee, String ipAddr,  CjLotteryActivity activity);

    CjResult<Void> lotteryRecover(int userId, List<Integer> idList);

    void saveCallbackData(TransactionConsumeData data);

    void wxRefundBack(RefundConsumeData data);

    CjResult transportFare(int userId, int totalFee, String ipAddr, List<Integer> idList);

    CjResult wxTransportPay(int userId, int totalFee, String ipAddr, List<Integer> idList);

    CjResult<PaySuccessVo> createAliH5OrderPay(int userId, int totalFee, String ipAddr, CjLotteryActivity activity, HttpServletResponse response);

    /**
     * 回收
     * @param userId
     * @param idList
     * @return
     */
    CjResult<Void> lotteryAliH5Recorver(int userId, List<Integer> idList);

    CjResult aliTransportPay(int userId, int totalFee, String ipAddr, List<Integer> idList);

    CjResult<Boolean> manHandleRefund(int userId, String outTradeNo);

    void saveAliCallbackData(Map<String, String> params,CjOrderPay orderPay);
}
