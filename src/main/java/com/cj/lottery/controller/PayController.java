package com.cj.lottery.controller;


import cn.felord.payment.wechat.v3.WechatApiProvider;
import cn.felord.payment.wechat.v3.model.ResponseSignVerifyParams;
import com.cj.lottery.constant.WxCons;
import com.cj.lottery.dao.CjOrderPayDao;
import com.cj.lottery.domain.CjNotifyPay;
import com.cj.lottery.domain.CjOrderPay;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.PaySuccessVo;
import com.cj.lottery.enums.ErrorEnum;
import com.cj.lottery.service.OrderPayService;
import com.cj.lottery.service.ProductInfoService;
import com.cj.lottery.util.ContextUtils;
import com.cj.lottery.util.IpUtil;
import com.google.common.cache.Cache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 支付controller
 *
 * @author lyq
 */
@Api(value = "支付",description = "支付相关接口")
@RestController
@Slf4j
@RequestMapping("api/pay")
public class PayController {

    @Autowired
    private OrderPayService orderPayService;
//    @Autowired
//    public WechatApiProvider wechatApiProvider;
    @Autowired
    public Cache<String, String> cache;
    @Autowired
    private ProductInfoService productInfoService;
    @Autowired
    private CjOrderPayDao orderPayDao;



    @ApiOperation("微信公众号充值接口")
    @PostMapping("wxGzhPay")
    public CjResult wxgzhPay(@ApiParam("充值金额(分)") @RequestParam int totalFee,
                             @ApiParam("活动编码")@RequestParam String activityCode) {
        int userId = ContextUtils.getUserId();
        return orderPayService.createWxOrderPay(userId, totalFee,activityCode);

    }


    @ApiOperation("微信H5充值接口")
    @PostMapping("wxH5Pay")
    public CjResult<PaySuccessVo> wxH5Pay(HttpServletRequest request,
                                          @ApiParam("充值金额(分)") @RequestParam int totalFee,
                                          @ApiParam("活动编码")@RequestParam String activityCode) {
        int userId = ContextUtils.getUserId();
        String ipAddr = IpUtil.getIpAddr(request);
        if (ObjectUtils.isEmpty(ipAddr)){
            return CjResult.fail(ErrorEnum.IP_ERROR);
        }
        return orderPayService.createWxH5OrderPay(userId, totalFee,ipAddr,activityCode);

    }

    @ApiOperation("奖品运费支付")
    @PostMapping("prize-transport-fare")
    public CjResult prizeTransportFare(HttpServletRequest request,
                                       @ApiParam("充值金额(分)") @RequestParam int totalFee,
                                       @ApiParam("奖品记录唯一标识") @RequestParam List<Integer> idList) {
        int userId = ContextUtils.getUserId();
        String ipAddr = IpUtil.getIpAddr(request);
        if (ObjectUtils.isEmpty(ipAddr)){
            return CjResult.fail(ErrorEnum.IP_ERROR);
        }
        return orderPayService.transportPay(userId, totalFee,ipAddr,idList);

    }

    @ApiOperation("奖品回收接口")
    @PostMapping("lottery-recover")
    public CjResult<Void> lotterRecover(@ApiParam("奖品记录唯一标识") @RequestParam List<Integer> idList) {

        return orderPayService.lotteryRecover(ContextUtils.getUserId(),idList);

    }

    @ApiOperation("微信支付回调接口")
    @PostMapping("callbacks/transaction")
    public Map<String, ?> wxCallbacks(@RequestHeader("Wechatpay-Serial") String wechatpaySerial,
                                   @RequestHeader("Wechatpay-Signature") String wechatpaySignature,
                                   @RequestHeader("Wechatpay-Timestamp") String wechatpayTimestamp,
                                   @RequestHeader("Wechatpay-Nonce") String wechatpayNonce,
                                   HttpServletRequest request) {

        try{
            String body = request.getReader().lines().collect(Collectors.joining());
            // 对请求头进行验签 以确保是微信服务器的调用
            ResponseSignVerifyParams params = new ResponseSignVerifyParams();
            params.setWechatpaySerial(wechatpaySerial);
            params.setWechatpaySignature(wechatpaySignature);
            params.setWechatpayTimestamp(wechatpayTimestamp);
            params.setWechatpayNonce(wechatpayNonce);
            params.setBody(body);
//            return wechatApiProvider.callback(WxCons.tenantId).transactionCallback(params, data -> {
//                //TODO 对回调解析的结果进行消费  需要保证消费的幂等性 微信有可能多次调用此接口
//                //先保存到缓存中
//                String transactionId = data.getTransactionId(); //微信订单号
//                String smsCode = cache.getIfPresent(transactionId);
//                if (!ObjectUtils.isEmpty(smsCode)){
//                    //存在直接返回成功
//                    return;
//                }
//                cache.put(transactionId,transactionId);
//                orderPayService.saveCallbackData(data);
//                if (!ObjectUtils.isEmpty(data.getAttach())){
//                    //todo 进行发货
//                    List<Integer> idList = Lists.newArrayList(data.getAttach().split(",")).stream().map(s->Integer.valueOf(s)).collect(Collectors.toList());
//                    CjOrderPay orderPay = orderPayDao.selectByOutTradeNo(data.getOutTradeNo());
//                    productInfoService.sendGoods(idList,orderPay.getCustomerId());
//                }
//            });
        }catch (Exception ex){
            log.info("wxCallbacks exception:",ex);
        }
        return null;
    }


    @ApiOperation("微信退款回调接口")
    @PostMapping("callbacks/refund")
    public Map<String, ?> wxRefund(@RequestHeader("Wechatpay-Serial") String wechatpaySerial,
                                      @RequestHeader("Wechatpay-Signature") String wechatpaySignature,
                                      @RequestHeader("Wechatpay-Timestamp") String wechatpayTimestamp,
                                      @RequestHeader("Wechatpay-Nonce") String wechatpayNonce,
                                      HttpServletRequest request) {

        try{
            String body = request.getReader().lines().collect(Collectors.joining());
            // 对请求头进行验签 以确保是微信服务器的调用
            ResponseSignVerifyParams params = new ResponseSignVerifyParams();
            params.setWechatpaySerial(wechatpaySerial);
            params.setWechatpaySignature(wechatpaySignature);
            params.setWechatpayTimestamp(wechatpayTimestamp);
            params.setWechatpayNonce(wechatpayNonce);
            params.setBody(body);
//            return wechatApiProvider.callback(WxCons.tenantId).refundCallback(params, data -> {
//                //TODO 对回调解析的结果进行消费  需要保证消费的幂等性 微信有可能多次调用此接口
//                //先保存到缓存中
//                String transactionId = data.getTransactionId(); //微信订单号
//                String smsCode = cache.getIfPresent(transactionId);
//                if (!ObjectUtils.isEmpty(smsCode)){
//                    //存在直接返回成功
//                    return;
//                }
//                cache.put(transactionId,transactionId);
//                orderPayService.wxRefundBack(data);
//
//            });
        }catch (Exception ex){
            log.info("wxRefund exception:",ex);
        }
        return null;
    }


    @ApiOperation("查询订单是否支付成功")
    @PostMapping("query-order-status")
    public CjResult<Boolean> queryOrderStatus(HttpServletRequest request,
                                     @RequestParam String outTradeNo) {
        int userId = ContextUtils.getUserId();
        if (ObjectUtils.isEmpty(outTradeNo)){
            return orderPayService.queryLatestOrderStatus(userId);
        }
        return orderPayService.queryOrderByUserIdAndOutTradeNo(userId,outTradeNo);

    }

}
