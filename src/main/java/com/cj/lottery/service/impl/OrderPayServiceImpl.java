package com.cj.lottery.service.impl;

import cn.felord.payment.wechat.enumeration.RefundStatus;
import cn.felord.payment.wechat.enumeration.TradeState;
import cn.felord.payment.wechat.v3.WechatApiProvider;
import cn.felord.payment.wechat.v3.WechatResponseEntity;
import cn.felord.payment.wechat.v3.model.*;
import com.alibaba.fastjson.JSONObject;
import com.cj.lottery.constant.WxCons;
import com.cj.lottery.dao.*;
import com.cj.lottery.domain.*;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.PaySuccessVo;
import com.cj.lottery.enums.*;
import com.cj.lottery.event.EventPublishService;
import com.cj.lottery.service.OrderPayService;
import com.cj.lottery.service.ProductInfoService;
import com.cj.lottery.util.DateUtil;
import com.cj.lottery.util.UuidUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Slf4j
public class OrderPayServiceImpl implements OrderPayService {

    @Value("${app.weixin.pay.appid}")
    private String appid;

    @Value("${app.weixin.pay.mchid}")
    private String mchid;

    @Value("${app.weixin.pay.notifyUrl}")
    private String notifyUrl;

    @Value("${app.weixin.refund.notifyUrl}")
    private String refundNotifyUrl;

    @Autowired
    private EventPublishService eventPublishService;

    private Integer zhekouRate = 70;

    @Autowired
    private CjOrderPayDao orderPayDao;

    @Autowired
    private CjCustomerLoginDao cjCustomerLoginDao;

//    @Autowired
//    public WechatApiProvider wechatApiProvider;

    @Autowired
    private CjLotteryActivityDao cjLotteryActivityDao;

    @Autowired
    private CjLotteryRecordDao cjLotteryRecordDao;

    @Autowired
    private CjProductInfoDao productInfoDao;
    @Autowired
    private CjOrderRefundDao orderRefundDao;
    @Autowired
    private CjNotifyPayDao notifyPayDao;
    @Autowired
    private ProductInfoService productInfoService;

    @Override
    public CjResult<String> createWxOrderPay(int customerId, int totalFee, String activityCode) {
        CjLotteryActivity activity = cjLotteryActivityDao.selectActivityByCode(activityCode);
        if (null == activity) {
            return CjResult.fail(ErrorEnum.NOT_ACITVITY);
        }
        totalFee = totalFee * 100;
        CjCustomerLogin login = cjCustomerLoginDao.selectById(customerId);
        if (login == null) {
            return CjResult.fail(ErrorEnum.USERINFO_NOT_EXIST);
        }
        String openid = login.getLoginPhone();
        String out_trade_no = UuidUtils.getOrderNo();

        this.jsPay(totalFee, out_trade_no, openid, activity.getActivityName());
        CjOrderPay cjOrderPay = this.buildOrderPayDO(customerId, totalFee, out_trade_no, activity.getActivityName(), PayTypeEnum.WX_GZH);
        return CjResult.success(null);
    }

    public void jsPay(int totalFee, String out_trade_no, String openId, String payDesc) {
        PayParams payParams = new PayParams();

        payParams.setDescription(payDesc);
        payParams.setOutTradeNo(out_trade_no);
        // 需要定义回调通知
        // 参考 CallbackController
        payParams.setNotifyUrl(notifyUrl);
        Amount amount = new Amount();
        amount.setTotal(totalFee);
        payParams.setAmount(amount);
        // 此类支付  Payer 必传  且openid需要同appid有绑定关系 具体去看文档
        cn.felord.payment.wechat.v3.model.Payer payer = new cn.felord.payment.wechat.v3.model.Payer();
        payer.setOpenid(openId);
        payParams.setPayer(payer);
//        WechatResponseEntity<ObjectNode> responseEntity = wechatApiProvider.directPayApi(WxCons.tenantId).jsPay(payParams);
//        Assertions.assertThat(responseEntity.is2xxSuccessful()).isTrue();
//
//        System.out.println("responseEntity = " + responseEntity);
    }


//    @Override
//    public CjResult<Void> wxOrderNotify() {
//        return null;
//    }


    @Override
    public CjResult<Boolean> queryLatestOrderStatus(int customerId) {
        //查询用户最近一分钟一条订单
        Date date = DateUtil.addMinute(new Date(), -2);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        String startTime = sdf.format(date);
        CjOrderPay cjOrderPay = orderPayDao.selectLatestOrder(customerId, startTime);
        return this.checkOrder(cjOrderPay);
    }

    @Override
    public CjResult<Boolean> queryOrderByUserIdAndOutTradeNo(int customerId, String outTradeNo) {
        CjOrderPay cjOrderPay = orderPayDao.selectByUserIdAndOutTradeNo(customerId, outTradeNo);
        return this.checkOrder(cjOrderPay);
    }

    @Override
    public CjResult<PaySuccessVo> createWxH5OrderPay(int userId, int totalFee, String ipAddr, String activityCode) {
        PaySuccessVo vo = new PaySuccessVo();
        CjLotteryActivity activity = cjLotteryActivityDao.selectActivityByCode(activityCode);
        if (null == activity) {
            return CjResult.fail(ErrorEnum.NOT_ACITVITY);
        }
        if (activity.getConsumerMoney() != totalFee){
            return CjResult.fail(ErrorEnum.PAY_MONEY_ERROR);
        }
        String out_trade_no = UuidUtils.getOrderNo();
        vo.setOutTradeNo(out_trade_no);
        this.h5Pay(activity.getActivityName(), totalFee, out_trade_no, ipAddr,null);
        //保存订单
        CjOrderPay cjOrderPay = this.buildOrderPayDO(userId, totalFee, out_trade_no, activity.getActivityName(), PayTypeEnum.WX_H5);
        return CjResult.success(vo);
    }

    @Override
    public CjResult<Object> transportPay(int userId, int totalFee, String ipAddr, List<Integer> idList){
        String out_trade_no = UuidUtils.getOrderNo();
        this.h5Pay("奖品运费", totalFee, out_trade_no, ipAddr,idList);
        //保存订单
        CjOrderPay cjOrderPay = this.buildOrderPayDO(userId, totalFee, out_trade_no, "奖品运费", PayTypeEnum.WX_H5);
        return CjResult.success();
    }

    private void h5Pay(String payDesc, int totalFee, String out_trade_no, String ipAddr,List<Integer> idList) {
        PayParams payParams = new PayParams();
        if (!CollectionUtils.isEmpty(idList)) {
            payParams.setAttach(JSONObject.toJSONString(idList));
        }
        payParams.setDescription(payDesc);
        payParams.setOutTradeNo(out_trade_no);
        // 需要定义回调通知
        payParams.setNotifyUrl(notifyUrl);

        Amount amount = new Amount();
        amount.setTotal(totalFee);
        payParams.setAmount(amount);

        // h5支付需要传递场景信息 具体去看文档 这里只写必填项
        SceneInfo sceneInfo = new SceneInfo();
        sceneInfo.setPayerClientIp(ipAddr);
        H5Info h5Info = new H5Info();
        // 只有类型是必填项
        h5Info.setType(H5Info.H5SceneType.Wap);
        sceneInfo.setH5Info(h5Info);
        payParams.setSceneInfo(sceneInfo);

//        WechatResponseEntity<ObjectNode> responseEntity = wechatApiProvider.directPayApi(WxCons.tenantId).h5Pay(payParams);
//
//        ObjectNode body = responseEntity.getBody();
//        System.out.println(JSONObject.toJSON(body));
    }

    @Override
    public CjResult<Void> lotteryRecover(int userId, List<Integer> idList) {
        List<CjLotteryRecord> recordList = cjLotteryRecordDao.selectByIdList(idList);
        if (CollectionUtils.isEmpty(recordList)) {
            return CjResult.fail(ErrorEnum.PRIZE_BELONG_ERROR);
        }
        List<CjOrderPay> payList = Lists.newArrayList();
        //校验奖品状态是否正常
        for (CjLotteryRecord record : recordList) {
            if (record.getCustomerId() != userId){
                return CjResult.fail(ErrorEnum.SYSTEM_ERROR);
            }
            //查询奖品状态
            if (record.getStatus() != PrizeStatusEnum.dai_fa_huo.getCode()) {
                return CjResult.fail(ErrorEnum.LOTTER_USERD);
            }
            CjOrderPay pay = orderPayDao.selectById(record.getOrderId());
            if (pay.getStatus() == PayStatusEnum.REFUND.getCode()) {
                return CjResult.fail(ErrorEnum.RECOVER);
            }
            if (pay.getStatus() != PayStatusEnum.USED.getCode()) {
                return CjResult.fail(ErrorEnum.ORDER_EXCEPTION);
            }
            payList.add(pay);
        }

        Map<Integer, CjOrderPay> orderMap = payList.stream().collect(Collectors.toMap(CjOrderPay::getId, Function.identity()));
        List<Integer> productIdList = recordList.stream().map(CjLotteryRecord::getProductId).collect(Collectors.toList());
        List<CjProductInfo> cjProductInfos = productInfoDao.selectByIds(productIdList);
        Map<Integer, Integer> rateMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(cjProductInfos)) {
            rateMap = cjProductInfos.stream().collect(Collectors.toMap(CjProductInfo::getId, CjProductInfo::getCallbackRate));
        }
        //循环进行退款操作
        for (CjLotteryRecord record : recordList) {
            //获取订单信息
            CjOrderPay orderPay = orderMap.get(record.getOrderId());
            //获取折扣率
            Integer rate = rateMap.get(record.getProductId());
            if (rate == null) {
                //兜底
                rate = zhekouRate;
            }

            RefundParams payParams = new RefundParams();
            String refundOrderNo = UuidUtils.getOrderNo();
//            payParams.setTransactionId(orderPay.getTransactionId());
            payParams.setOutTradeNo(orderPay.getOutTradeNo());
            payParams.setOutRefundNo(refundOrderNo);

            // 需要定义回调通知
            payParams.setNotifyUrl(refundNotifyUrl);
            RefundParams.RefundAmount amount = new RefundParams.RefundAmount();
            amount.setTotal(orderPay.getTotalFee());
            Integer refundFee = (orderPay.getTotalFee() * 100) / rate;
            amount.setRefund(refundFee);
            amount.setCurrency("CNY");
            payParams.setAmount(amount);
//            WechatResponseEntity<ObjectNode> responseEntity = wechatApiProvider.directPayApi(WxCons.tenantId).refund(payParams);
//            ObjectNode body = responseEntity.getBody();
//            log.info("lotteryRecover data:{}",JSONObject.toJSON(body));

            //如果成功
            cjLotteryRecordDao.updateStatusById(PrizeStatusEnum.yi_hui_shou.getCode(), record.getId());
            //暂不更新订单的状态，通过退款回调更新订单的状态
            this.createRefundOrder(orderPay, refundOrderNo, userId, refundFee);
        }
        return CjResult.success();
    }

    @Override
    public void saveCallbackData(TransactionConsumeData data) {
        String outTradeNo = data.getOutTradeNo();
        CjOrderPay orderPay = orderPayDao.selectByOutTradeNo(outTradeNo);
        if (orderPay.getStatus() != PayStatusEnum.NO_PAY.getCode()) {
            log.info("saveCallbackData 支付回调，订单状态异常，orderPay:{},wxNotifyBean:{}", JSONObject.toJSON(orderPay), JSONObject.toJSON(data));
            return;
        }
        //回调状态不为成功的话，不记录，直接返回
        if (data.getTradeState() != TradeState.SUCCESS) {
            log.info("saveCallbackData 支付状态异常，wxNotifyBean:{}", JSONObject.toJSON(data));
            return;
        }
        this.createPayNotify(data, orderPay);
        //更新下单状态
        orderPayDao.updateStatusById(orderPay.getId(),PayStatusEnum.PAY.getCode());
        //统计充值
        eventPublishService.addMoney(this,orderPay.getCustomerId(),data.getAmount().getTotal(), ScoreTypeEnum.ADD);
    }

    @Override
    public void wxRefundBack(RefundConsumeData data) {
        String outTradeNo = data.getOutTradeNo();
        CjOrderPay orderPay = orderPayDao.selectByOutTradeNo(outTradeNo);
        if( data.getRefundStatus() != RefundStatus.SUCCESS){
            log.info("wxRefundBack 退款回调状态异常，data:{}",JSONObject.toJSON(data));
            //查询奖品记录是否已回收，如果回收更新成待发货
            CjLotteryRecord record = cjLotteryRecordDao.selectByOrderId(orderPay.getId());
            if (record != null && record.getStatus() == PrizeStatusEnum.yi_hui_shou.getCode()){
                cjLotteryRecordDao.updateStatusById(PrizeStatusEnum.dai_fa_huo.getCode(),record.getId());
            }
            return;
        }
        orderPayDao.updateStatusById(orderPay.getId(), PayStatusEnum.REFUND.getCode());
        notifyPayDao.updateStatusByOutTradeNo(orderPay.getOutTradeNo(), PayStatusEnum.REFUND.getCode());

    }

    @Override
    public CjResult transportFare(int userId, int totalFee, String ipAddr, List<Integer> idList) {

        return null;
    }

    /**
     * 保存支付成功信息
     *
     * @param data
     * @param orderPay
     */
    private void createPayNotify(TransactionConsumeData data, CjOrderPay orderPay) {

        CjNotifyPay notifyPay = new CjNotifyPay();
        notifyPay.setMchId(data.getMchid());
        notifyPay.setAppid(data.getAppid());
        notifyPay.setCustomerId(orderPay.getCustomerId());
        notifyPay.setOutTradeNo(data.getOutTradeNo());
        notifyPay.setTradeType(orderPay.getTradeType());
        notifyPay.setStatus(PayStatusEnum.PAY.getCode());
        notifyPay.setTransactionId(data.getTransactionId());
        notifyPay.setTimeEnd(Date.from(data.getSuccessTime().toInstant()));
        notifyPay.setTotalFee(data.getAmount().getTotal());
        notifyPayDao.insertSelective(notifyPay);

    }

    /**
     * 创建退款记录表
     *
     * @param orderPay
     * @param refundOrderNo
     * @param userId
     * @param refundFee
     */
    private void createRefundOrder(CjOrderPay orderPay, String refundOrderNo, int userId, int refundFee) {
        CjOrderRefund refund = new CjOrderRefund();
        refund.setOrderId(orderPay.getId());
        refund.setCustomerId(userId);
        refund.setOutTradeNo(orderPay.getOutTradeNo());
        refund.setOutRefundNo(refundOrderNo);
        refund.setRefund(refundFee);
        refund.setTotalFee(orderPay.getTotalFee());
        refund.setMchId(orderPay.getMchId());
        refund.setAppid(orderPay.getAppid());
        orderRefundDao.insertSelective(refund);
    }


    private CjResult<Boolean> checkOrder(CjOrderPay cjOrderPay) {
        if (cjOrderPay == null) {
            return CjResult.fail(ErrorEnum.NOT_ORDER);
        }
        if (cjOrderPay.getStatus() == PayStatusEnum.NO_PAY.getCode()) {
            return CjResult.fail(ErrorEnum.NO_PAY);
        }
        if (cjOrderPay.getStatus() == PayStatusEnum.REFUND.getCode()) {
            return CjResult.fail(ErrorEnum.REFUND);
        }
        if (cjOrderPay.getStatus() == PayStatusEnum.USED.getCode()) {
            return CjResult.fail(ErrorEnum.USED);
        }
        if (cjOrderPay.getStatus() == PayStatusEnum.PAY.getCode()) {
            return CjResult.success(true);
        }
        return CjResult.fail(ErrorEnum.SYSTEM_ERROR);
    }


    /**
     * 构建订单表参数
     *
     * @param customerId
     * @param totalFee
     * @param out_trade_no
     * @return
     */
    private CjOrderPay buildOrderPayDO(int customerId, int totalFee, String out_trade_no, String description, PayTypeEnum payType) {
        CjOrderPay orderPay = new CjOrderPay();
        orderPay.setAppid(appid);
        orderPay.setBody(description);
        orderPay.setCustomerId(customerId);
        orderPay.setMchId(mchid);
        orderPay.setOutTradeNo(out_trade_no);
        //todo 晚些改回未支付状态
        orderPay.setStatus(PayStatusEnum.PAY.getCode());
        orderPay.setTradeType(payType.getCode());
        orderPay.setTotalFee(totalFee);
        orderPayDao.insertSelective(orderPay);
        return orderPay;
    }

}
