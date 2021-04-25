package com.cj.lottery.controller;


import cn.felord.payment.wechat.enumeration.TradeState;
import cn.felord.payment.wechat.v3.WechatApiProvider;
import cn.felord.payment.wechat.v3.model.ResponseSignVerifyParams;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;
import com.cj.lottery.constant.AliCons;
import com.cj.lottery.constant.WxCons;
import com.cj.lottery.dao.CjCustomerInfoDao;
import com.cj.lottery.dao.CjOrderPayDao;
import com.cj.lottery.domain.CjCustomerInfo;
import com.cj.lottery.domain.CjOrderPay;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.PaySuccessVo;
import com.cj.lottery.enums.ErrorEnum;
import com.cj.lottery.enums.PayStatusEnum;
import com.cj.lottery.enums.PayTypeEnum;
import com.cj.lottery.enums.ScoreTypeEnum;
import com.cj.lottery.event.EventPublishService;
import com.cj.lottery.service.OrderPayService;
import com.cj.lottery.service.ProductInfoService;
import com.cj.lottery.service.UserInfoService;
import com.cj.lottery.util.*;
import com.google.common.cache.Cache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
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
    @Autowired
    public WechatApiProvider wechatApiProvider;
    @Autowired
    public Cache<String, String> cache;
    @Autowired
    private ProductInfoService productInfoService;
    @Autowired
    private CjOrderPayDao orderPayDao;
    @Value("${ali.pay.v1.alipay-public-cert-path}")
    private String aliPublicKey ;

    @Autowired
    private EventPublishService eventPublishService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private CjCustomerInfoDao customerInfoDao;

    @ApiOperation("微信公众号充值接口")
    @PostMapping("wxGzhPay")
    public CjResult wxgzhPay(@ApiParam("充值金额(分)") @RequestParam int totalFee,
                             @ApiParam("活动编码")@RequestParam String activityCode) {
        int userId = ContextUtils.getUserId();
        return orderPayService.createWxOrderPay(userId, totalFee,activityCode);

    }


    @ApiOperation("H5充值接口")
    @PostMapping("wxH5Pay")
    public CjResult<PaySuccessVo> wxH5Pay(HttpServletRequest request,
                                          HttpServletResponse response,
                                          @ApiParam("充值金额(分)") @RequestParam int totalFee,
                                          @ApiParam("活动编码")@RequestParam String activityCode,
                                          @ApiParam("支付类型")@RequestParam(required = false,defaultValue = "WX_H5")PayTypeEnum payType) {

        int userId = ContextUtils.getUserId();
        String ipAddr = IpUtil.getIpAddr(request);
        if (ObjectUtils.isEmpty(ipAddr)){
            return CjResult.fail(ErrorEnum.IP_ERROR);
        }
        if (PayTypeEnum.WX_H5 == payType){
            return orderPayService.createWxH5OrderPay(userId, totalFee,ipAddr,activityCode);
        }else if (PayTypeEnum.ALI_H5 == payType){
            return orderPayService.createAliH5OrderPay(userId, totalFee,ipAddr,activityCode,response);
        }
        return CjResult.fail(ErrorEnum.PAY_TYPE_ERROR);

    }

    @ApiOperation("奖品运费支付")
    @PostMapping("prize-transport-fare")
    public CjResult<PaySuccessVo> prizeTransportFare(HttpServletRequest request,
                                       @ApiParam("充值金额(分)") @RequestParam int totalFee,
                                       @ApiParam("奖品记录唯一标识") @RequestParam List<Integer> idList,
                                       @ApiParam("支付类型")@RequestParam(required = false,defaultValue = "WX_H5")PayTypeEnum payType) {
        int userId = ContextUtils.getUserId();
        String ipAddr = IpUtil.getIpAddr(request);
        if (ObjectUtils.isEmpty(ipAddr)){
            return CjResult.fail(ErrorEnum.IP_ERROR);
        }
        if (PayTypeEnum.WX_H5 == payType){
            return orderPayService.wxTransportPay(userId, totalFee,ipAddr,idList);
        }else if (PayTypeEnum.ALI_H5 == payType){
            return orderPayService.aliTransportPay(userId, totalFee,ipAddr,idList);
        }
        return CjResult.fail(ErrorEnum.PAY_TYPE_ERROR);
    }

    @ApiOperation("奖品回收接口")
    @PostMapping("lottery-recover")
    public CjResult<Void> lotterRecover(@ApiParam("奖品记录唯一标识") @RequestParam List<Integer> idList) {

        //todo 新人活动不能回收
        return orderPayService.lotteryRecover(ContextUtils.getUserId(), idList);

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
            log.info("wxCallbacks start param:{},httpBody:{}", JSON.toJSONString(params),body);
            return wechatApiProvider.callback(WxCons.tenantId).transactionCallback(params, data -> {
                //TODO 对回调解析的结果进行消费  需要保证消费的幂等性 微信有可能多次调用此接口
                //回调状态不为成功的话，不记录，直接返回
                if (data.getTradeState() != TradeState.SUCCESS) {
                    log.info("saveCallbackData 支付状态异常，wxNotifyBean:{}", JSONObject.toJSON(data));
                    return;
                }
                //先保存到缓存中
                String id = JSONObject.parseObject(body).getString("id"); //微信订单号
                String smsCode = cache.getIfPresent(id);
                if (!ObjectUtils.isEmpty(smsCode) ){
                    log.info("wxCallbacks 重复通知--id：{}",id);
                    //存在直接返回成功
                    return;
                }
                log.info("--------wxCallbacks success----id:{}",id);
                cache.put(id,id);
                orderPayService.saveCallbackData(data);
                if (!ObjectUtils.isEmpty(data.getAttach())){
                    //todo 进行发货
                    List<Integer> idList = JSONObject.parseArray(data.getAttach()).stream().map(s->Integer.parseInt(s.toString())).collect(Collectors.toList());
                    CjOrderPay orderPay = orderPayDao.selectByOutTradeNo(data.getOutTradeNo());
                    productInfoService.sendGoods(idList,orderPay.getCustomerId(),true);
                }
            });
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
            log.info("wxRefund param:{},body:{}",JSON.toJSONString(params),body);
            return wechatApiProvider.callback(WxCons.tenantId).refundCallback(params, data -> {
                //TODO 对回调解析的结果进行消费  需要保证消费的幂等性 微信有可能多次调用此接口
                //先保存到缓存中
                log.info("wxRefund success data:{}",JSON.toJSONString(data));
                String id = JSONObject.parseObject(body).getString("id"); //微信订单号
                String smsCode = cache.getIfPresent(id);
                if (!ObjectUtils.isEmpty(smsCode)){
                    //存在直接返回成功
                    return;
                }
                cache.put(id,id);
                orderPayService.wxRefundBack(data);

            });
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


    @ApiOperation("手动退款接口")
    @PostMapping("handle-refund")
    public CjResult<Boolean> handleRefund(HttpServletRequest request,
                                              @RequestParam String outTradeNo) {
        int userId = ContextUtils.getUserId();
        return orderPayService.manHandleRefund(userId,outTradeNo);
    }

    @ApiOperation("微信退款回调接口")
    @PostMapping("callbacks/manHandelrefund")
    public Map<String, ?> wxManHandleRefund(@RequestHeader("Wechatpay-Serial") String wechatpaySerial,
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
            log.info("wxRefund param:{},body:{}",JSON.toJSONString(params),body);
            return wechatApiProvider.callback(WxCons.tenantId).refundCallback(params, data -> {
                //TODO 对回调解析的结果进行消费  需要保证消费的幂等性 微信有可能多次调用此接口
                //先保存到缓存中
                log.info("wxRefund success data:{}",JSON.toJSONString(data));
                String id = JSONObject.parseObject(body).getString("id"); //微信订单号
                String smsCode = cache.getIfPresent(id);
                if (!ObjectUtils.isEmpty(smsCode)){
                    //存在直接返回成功
                    return;
                }
                cache.put(id,id);
                orderPayService.wxRefundBack(data);
            });
        }catch (Exception ex){
            log.info("wxRefund exception:",ex);
        }
        return null;
    }

    @ApiOperation("支付宝支付成功回调接口")
    @PostMapping("callbacks/aliNotify")
    public String aliNotify(HttpServletRequest request, HttpServletResponse out) {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        try {
            Map requestParams = request.getParameterMap();
            for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i]
                            : valueStr + values[i] + ",";
                }
                params.put(name, valueStr);
            }
            String out_trade_no = params.get("out_trade_no");
            //支付宝交易号
            String trade_no = params.get("trade_no");
            //交易状态
            String trade_status = params.get("trade_status");
            log.info("aliNotify 封装param 结果:{}", JSONObject.toJSON(params));
            boolean verify_result = AlipaySignature.rsaCertCheckV1(params, FileUtils.getFileAbsolutePath(aliPublicKey), AliCons.CHARSET, AliCons.SIGNTYPE);
            log.info("aliNotify verfy result 签名结果:{}", verify_result);
            if (verify_result) {//验证成功
                if (trade_status.equals("TRADE_SUCCESS")) {
                    String smsCode = cache.getIfPresent(trade_no);
                    if (!ObjectUtils.isEmpty(smsCode)) {
                        log.info("aliCallbacks 重复通知--trade_no：{}", trade_no);
                        //存在直接返回成功
                        return "SUCCESS";
                    }
                    log.info("liCallbacks success----trade_no:{}", trade_no);
                    cache.put(trade_no, trade_no);
                    CjOrderPay orderPay = orderPayDao.selectByOutTradeNo(out_trade_no);
                    if (orderPay == null) {
                        log.info("aliCallbacks not query orderInfo trade_no:{}", trade_no);
                        return "FAIL";
                    }
                    if (orderPay.getStatus() != PayStatusEnum.NO_PAY.getCode()) {
                        log.info("saveAliCallbackData 支付回调，订单状态异常，orderPayStatus:{}", orderPay.getStatus());
                        return "FAIL";
                    }
                    String total_amount = params.get("total_amount");
                    if (Float.parseFloat(total_amount) * 100 != orderPay.getTotalFee()) {
                        log.info("aliCallbacks pay money exception, orderMoney:{} notifyMoney:{}", orderPay.getTotalFee(), Integer.parseInt(total_amount) * 100);
                        return "FAIL";
                    }
                    orderPayService.saveAliCallbackData(params, orderPay);
                    String attach = params.get("passback_params");
                    if (!ObjectUtils.isEmpty(attach)) {
                        //todo 进行发货
                        log.info("发货attach：{}",attach);
                        List<Integer> idList = Lists.newArrayList(attach.split(",")).stream().map(s -> Integer.parseInt(s.toString())).collect(Collectors.toList());
                        CjResult<Void> voidCjResult = productInfoService.sendGoods(idList, orderPay.getCustomerId(), true);
                        log.info("send goods data:{}",JSONObject.toJSON(voidCjResult));
                    }
                } else {
                    log.info("aliNotify order pay status exception：{}", trade_status);
                }
            } else {//验证失败
                String sWord = AlipaySignature.getSignCheckContentV1(params);
                log.info("aliNotify verfy fail data:{}", sWord);
            }
            return "SUCCESS";
        } catch (Exception ex) {
            log.info("aliNotify exception: param:{}", ex);
        }
        return "FAIL";
    }


    @GetMapping("callbacks/test")
    public void test(@RequestParam int userID,@RequestParam String score){
        CjCustomerInfo info = userInfoService.queryUserInfoByCustomerId(userID);
        CjCustomerInfo userInfo = new CjCustomerInfo();
        userInfo.setId(info.getId());
        userInfo.setScoreInFen(Float.valueOf(score) +Float.parseFloat( info.getScoreInFen()));
        customerInfoDao.updateByPrimaryKeySelective(userInfo);
//        HttpClientResult dataata = HttpClientUtils.doGet(url);
//        log.info("url:{}",data.getContent());
    }

}
