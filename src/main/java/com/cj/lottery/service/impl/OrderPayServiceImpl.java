package com.cj.lottery.service.impl;

import cn.felord.payment.wechat.enumeration.RefundStatus;
import cn.felord.payment.wechat.v3.WechatApiProvider;
import cn.felord.payment.wechat.v3.WechatResponseEntity;
import cn.felord.payment.wechat.v3.model.*;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
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
import com.cj.lottery.util.HttpClientResult;
import com.cj.lottery.util.HttpClientUtils;
import com.cj.lottery.util.UuidUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Slf4j
public class OrderPayServiceImpl implements OrderPayService {

    @Value("${wechat.pay.v3.lottery.app-id}")
    private String appid;

    @Value("${wechat.pay.v3.lottery.mch-id}")
    private String mchid;

    private String notifyUrl = "/lottery/api/pay/callbacks/transaction";

    @Value("${ali.pay.v1.notify}")
    private String aliNotifyUrl;

    @Value("${app.weixin.refund.notifyUrl}")
    private String refundNotifyUrl;

    @Autowired
    private EventPublishService eventPublishService;

    private Integer zhekouRate = 80;

    @Autowired
    private CjOrderPayDao orderPayDao;

    @Autowired
    private CjCustomerLoginDao cjCustomerLoginDao;

    @Autowired
    public WechatApiProvider wechatApiProvider;

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

    @Autowired
    AlipayClient alipayClient;

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
        WechatResponseEntity<ObjectNode> responseEntity = wechatApiProvider.directPayApi(WxCons.tenantId).jsPay(payParams);
        Assertions.assertThat(responseEntity.is2xxSuccessful()).isTrue();

        System.out.println("responseEntity = " + responseEntity);
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
    public CjResult<PaySuccessVo> createWxH5OrderPay(int userId, int totalFee, String ipAddr, CjLotteryActivity activity) {
        PaySuccessVo vo = new PaySuccessVo();
//        CjLotteryActivity activity = cjLotteryActivityDao.selectActivityByCode(activityCode);
//        if (null == activity) {
//            return CjResult.fail(ErrorEnum.NOT_ACITVITY);
//        }
//        if (activity.getConsumerMoney() != totalFee){
//            return CjResult.fail(ErrorEnum.PAY_MONEY_ERROR);
//        }
        String out_trade_no = UuidUtils.getOrderNo();
        vo.setOutTradeNo(out_trade_no);
        String h5Url= this.h5Pay(activity.getActivityName(), totalFee, out_trade_no, ipAddr, null);

        vo.setH5_url(h5Url);
        //拼接上跳转地址
//        h5Url = h5Url +"&redirect_url="+ URLEncoder.encode("https://m.keyundz.cn/#/goodsDetail?goodId="+activityCode);
//        HttpClientResult data = HttpClientUtils.doGet(h5Url);
//        log.info("url:{}",data.getContent());
//        String content = data.getContent();
//        content = content.substring(content.indexOf("var url=")+9, content.indexOf("\n" +
//                "                    var redirect_url")-2);
//        log.info("新连接url：{}",content);
//        vo.setH5_url(content);
        //保存订单
        CjOrderPay cjOrderPay = this.buildOrderPayDO(userId, totalFee, out_trade_no, activity.getActivityName(), PayTypeEnum.WX_H5);
        return CjResult.success(vo);
    }

    @Override
    public CjResult<PaySuccessVo> wxTransportPay(int userId, int totalFee, String ipAddr, List<Integer> idList){
        PaySuccessVo vo = new PaySuccessVo();
        String out_trade_no = UuidUtils.getOrderNo();
        String h5Url = this.h5Pay("商品邮费", totalFee, out_trade_no, ipAddr, idList);
        //保存订单
        vo.setH5_url(h5Url);
        vo.setOutTradeNo(out_trade_no);
        CjOrderPay cjOrderPay = this.buildOrderPayDO(userId, totalFee, out_trade_no, "奖品运费", PayTypeEnum.WX_H5);
        return CjResult.success(vo);
    }

    @Override
    public CjResult aliTransportPay(int userId, int totalFee, String ipAddr, List<Integer> idList) {

        PaySuccessVo vo = new PaySuccessVo();
        String body = "商品邮费";
        String money = intToFloat(totalFee);
        String out_trade_no = UuidUtils.getOrderNo();
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        request.setReturnUrl("https://m.keyundz.cn/#/paySuccess?idList="+ StringUtils.join(idList,",")+"&payMoney="+totalFee+"&orderNo="+out_trade_no);
        request.setNotifyUrl(aliNotifyUrl);//在公共参数中设置回跳和通知地址
        // 封装请求支付信息
        AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
        model.setOutTradeNo(out_trade_no);
        model.setSubject(body);
        model.setTotalAmount(money);
        // 销售产品码 必填
        model.setProductCode("QUICK_WAP_WAY");
        model.setQuitUrl("https://m.keyundz.cn/#/sureSend/confirmPay?payMoney="+totalFee+"&ids="+StringUtils.join(idList,","));
        model.setPassbackParams(StringUtils.join(idList,","));
        request.setBizModel(model);
        try {
            String form = alipayClient.pageExecute(request).getBody();
            log.info("aliTransportPay execute = {}",JSONObject.toJSON(form));
            CjOrderPay cjOrderPay = this.buildOrderPayDO(userId, totalFee, out_trade_no, body, PayTypeEnum.ALI_H5);
            vo.setOutTradeNo(out_trade_no);
            vo.setH5_url(form);
            return CjResult.success(vo);
        } catch (Exception e) {
            log.info("aliTransportPay exception:",e);
        }
        return CjResult.success(vo);
    }

    @Override
    public CjResult<Boolean> manHandleRefund(int userId, String outTradeNo) {
        log.info("manHandleRefund param:{}",outTradeNo);
        CjOrderPay orderPay = orderPayDao.selectByOutTradeNo(outTradeNo);
        if(orderPay == null){
            return CjResult.fail("参数有误");
        }
        if (orderPay.getStatus() == PayStatusEnum.USED.getCode()){
            return CjResult.fail("已经核销，不能退款");
        }
        CjLotteryRecord record = cjLotteryRecordDao.selectByOrderId(orderPay.getId());
//        if (record == null){
//            return CjResult.fail("订单信息不存在");
//        }
        if (orderPay.getTradeType() == PayTypeEnum.WX_H5.getCode()){
            this.wxRefund(orderPay,100,record,userId,false);
        }else if (orderPay.getTradeType() == PayTypeEnum.ALI_H5.getCode()){
            this.aliRefund(orderPay,100,record,userId,false);
        }else {
            return CjResult.fail(ErrorEnum.ORDER_PAY_TYPE);
        }
        return CjResult.success();
    }

    @Override
    public void saveAliCallbackData(Map<String, String> params,CjOrderPay orderPay) {
        this.createAliPayNotify(params, orderPay);
        //更新下单状态
        orderPayDao.updateStatusById(orderPay.getId(),PayStatusEnum.PAY.getCode());
        //统计充值
        eventPublishService.addMoney(this,orderPay.getCustomerId(),orderPay.getTotalFee(), ScoreTypeEnum.ADD);

    }

    private void createAliPayNotify(Map<String, String> params, CjOrderPay orderPay) {
        CjNotifyPay notifyPay = new CjNotifyPay();
        notifyPay.setMchId(params.get("app_id"));
        notifyPay.setAppid(params.get("app_id"));
        notifyPay.setCustomerId(orderPay.getCustomerId());
        notifyPay.setOutTradeNo(params.get("out_trade_no"));
        notifyPay.setTradeType(orderPay.getTradeType());
        notifyPay.setStatus(PayStatusEnum.PAY.getCode());
        notifyPay.setTransactionId(params.get("trade_no"));
        notifyPay.setTimeEnd(DateUtil.format(params.get("gmt_payment"),DateUtil.YYYY_MM_DD_HH_MM_SS));
        notifyPay.setTotalFee(orderPay.getTotalFee());
        notifyPayDao.insertSelective(notifyPay);

    }

    @SneakyThrows
    @Override
    public CjResult<PaySuccessVo> createAliH5OrderPay(int userId, int totalFee, String ipAddr, CjLotteryActivity activity, HttpServletResponse response) {
        PaySuccessVo vo = new PaySuccessVo();
        String money = intToFloat(totalFee);
        String out_trade_no = UuidUtils.getOrderNo();
        vo.setOutTradeNo(out_trade_no);
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();

        request.setReturnUrl("https://m.keyundz.cn/#/goodsDetail?goodId="+activity.getActivityCode());
        request.setNotifyUrl(aliNotifyUrl);//在公共参数中设置回跳和通知地址
        // 封装请求支付信息
        AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
        model.setOutTradeNo(out_trade_no);
        model.setSubject(activity.getActivityName());
        model.setTotalAmount(money);
        // 销售产品码 必填
        String product_code="QUICK_WAP_WAY";
        model.setProductCode(product_code);
        model.setQuitUrl("https://m.keyundz.cn/#/goodsDetail?goodId="+activity.getActivityCode());

        request.setBizModel(model);
        try {
            log.info("AlipayResponse param:{}",JSONObject.toJSON(request));
            String form = alipayClient.pageExecute(request).getBody();
            log.info("execute = {}",JSONObject.toJSON(form));
            CjOrderPay cjOrderPay = this.buildOrderPayDO(userId, totalFee, out_trade_no, activity.getActivityName(), PayTypeEnum.ALI_H5);
            vo.setOutTradeNo(out_trade_no);
            vo.setH5_url(form);
            return CjResult.success(vo);
        } catch (Exception e) {
            log.info("createAliH5OrderPay exception:",e);
        }
        return CjResult.success();
    }

    @Override
    public CjResult<Void> lotteryAliH5Recorver(int userId, List<Integer> idList) {

        List<CjLotteryRecord> recordList = cjLotteryRecordDao.selectByIdList(idList);
        if (CollectionUtils.isEmpty(recordList)) {
            return CjResult.fail(ErrorEnum.PRIZE_BELONG_ERROR);
        }
        List<CjOrderPay> payList = Lists.newArrayList();
        //校验奖品状态是否正常
        for (CjLotteryRecord record : recordList) {
            if (record.getCustomerId() != userId) {
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
        Set<Integer> activityIdSet = recordList.stream().map(CjLotteryRecord::getActivityId).collect(Collectors.toSet());
        List<CjLotteryActivity> activities = cjLotteryActivityDao.selectByIdList(Lists.newArrayList(activityIdSet));
        Map<Integer, Integer> rateMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(activities)) {
            Optional<CjLotteryActivity> first = activities.stream().filter(s -> s.getActivityFlag() == ActivityFlagEnum.NEW_PEPOLE.getCode()).findFirst();
            if (first.isPresent()) {
                return CjResult.fail(ErrorEnum.NEW_NOT_RECORVER);
            }
            rateMap = activities.stream().collect(Collectors.toMap(CjLotteryActivity::getId, CjLotteryActivity::getActivityRate));
        }
        //循环进行退款操作
        for (CjLotteryRecord record : recordList) {
            //获取订单信息
            CjOrderPay orderPay = orderMap.get(record.getOrderId());
            //获取折扣率
            Integer rate = rateMap.get(record.getActivityId());
            if (rate == null) {
                //兜底
                rate = zhekouRate;
            }
            String refundFee = intToFloat((orderPay.getTotalFee() * rate) / 100);
            int refundFeeInFen = (orderPay.getTotalFee() * rate) / 100;
            String refundOrderNo = UuidUtils.getOrderNo();
            try {
                AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();//创建API对应的request类
                AlipayTradeRefundModel model = new AlipayTradeRefundModel();
                model.setOutTradeNo(orderPay.getOutTradeNo());
                model.setRefundAmount(refundFee);
                model.setRefundReason("正常退款");
                model.setOutRequestNo(refundOrderNo);
                request.setBizModel(model);
                AlipayTradeRefundResponse response = alipayClient.execute(request);
                log.info("lotteryAliH5Recorver refund body:{}", response.getBody());
                if (response.isSuccess()) {
                    //todo 如果成功,调用支付宝查询接口查询是否退款成功

                    cjLotteryRecordDao.updateStatusById(PrizeStatusEnum.yi_hui_shou.getCode(), record.getId());
                    //支付宝退款接口没有回调通知，更新订单状态
                    this.createRefundOrder(orderPay, refundOrderNo, userId, refundFeeInFen);
                    orderPayDao.updateStatusById(orderPay.getId(), PayStatusEnum.REFUND.getCode());
                    notifyPayDao.updateStatusByOutTradeNo(orderPay.getOutTradeNo(), PayStatusEnum.REFUND.getCode());
                    eventPublishService.addMoney(this, orderPay.getCustomerId(), refundFeeInFen, ScoreTypeEnum.JIAN);
                }
            } catch (Exception ex) {
                log.info("lotteryAliH5Recorver fail,idList:{},userId:{},exception:", idList, userId, ex);
            }
        }
        return null;
    }


    private String h5Pay(String payDesc, int totalFee, String out_trade_no, String ipAddr,List<Integer> idList) {
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

        log.info("wxPay h5 order data:{}",JSONObject.toJSON(payParams));
        WechatResponseEntity<ObjectNode> responseEntity = wechatApiProvider.directPayApi(WxCons.tenantId).h5Pay(payParams);

        ObjectNode body = responseEntity.getBody();
        String h5_url = body.get("h5_url").asText();
        log.info("wxPay h5 url:{}",JSONObject.toJSON(h5_url));
        return h5_url;
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
        Set<Integer> activityIdSet = recordList.stream().map(CjLotteryRecord::getActivityId).collect(Collectors.toSet());
        List<CjLotteryActivity> activities = cjLotteryActivityDao.selectByIdList(Lists.newArrayList(activityIdSet));
        Map<Integer, Integer> rateMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(activities)) {
            Optional<CjLotteryActivity> first = activities.stream().filter(s -> s.getActivityFlag() == ActivityFlagEnum.NEW_PEPOLE.getCode()).findFirst();
            if (first.isPresent()){
                return CjResult.fail(ErrorEnum.NEW_NOT_RECORVER);
            }
            rateMap = activities.stream().collect(Collectors.toMap(CjLotteryActivity::getId, CjLotteryActivity::getActivityRate));
        }
        //循环进行退款操作
        for (CjLotteryRecord record : recordList) {
            //获取订单信息
            CjOrderPay orderPay = orderMap.get(record.getOrderId());
            //获取折扣率
            Integer rate = rateMap.get(record.getActivityId());
            if (rate == null) {
                //兜底
                rate = zhekouRate;
            }
            if (orderPay.getTradeType() == PayTypeEnum.WX_H5.getCode()){
                this.wxRefund(orderPay,rate,record,userId,true);
            }else if (orderPay.getTradeType() == PayTypeEnum.ALI_H5.getCode()){
                this.aliRefund(orderPay,rate,record,userId,true);
            }else {
                return CjResult.fail(ErrorEnum.ORDER_PAY_TYPE);
            }
            Integer refundFee = (orderPay.getTotalFee() * rate) / 100;
            eventPublishService.addMoney(this,orderPay.getCustomerId(),refundFee, ScoreTypeEnum.JIAN);
        }
        return CjResult.success();
    }

    private void wxRefund(CjOrderPay orderPay,Integer rate,CjLotteryRecord record,int userId,boolean flag){
            RefundParams payParams = new RefundParams();
            String refundOrderNo = UuidUtils.getOrderNo();
            payParams.setOutTradeNo(orderPay.getOutTradeNo());
            payParams.setOutRefundNo(refundOrderNo);

            // 需要定义回调通知
            payParams.setNotifyUrl(refundNotifyUrl);
            RefundParams.RefundAmount amount = new RefundParams.RefundAmount();
            amount.setTotal(orderPay.getTotalFee());
            Integer refundFee = (orderPay.getTotalFee() * rate) / 100;
            amount.setRefund(refundFee);
            amount.setCurrency("CNY");
            payParams.setAmount(amount);
            WechatResponseEntity<ObjectNode> responseEntity = wechatApiProvider.directPayApi(WxCons.tenantId).refund(payParams);
            ObjectNode body = responseEntity.getBody();
            log.info("lotteryRecover data:{}",JSONObject.toJSON(body));

            //如果成功
            if (flag) {
                cjLotteryRecordDao.updateStatusById(PrizeStatusEnum.yi_hui_shou.getCode(), record.getId());
            }
//            暂不更新订单的状态，通过退款回调更新订单的状态
            this.createRefundOrder(orderPay, refundOrderNo, userId, refundFee);
    }

    //todo 退款失败返回信息
    private void aliRefund(CjOrderPay orderPay,Integer rate,CjLotteryRecord record,int userId,boolean flag){
        String refundFee = intToFloat((orderPay.getTotalFee() * rate) / 100);
        int refundFeeInFen = (orderPay.getTotalFee() * rate) / 100;
        String refundOrderNo = UuidUtils.getOrderNo();
        try {
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();//创建API对应的request类
            AlipayTradeRefundModel model = new AlipayTradeRefundModel();
            model.setOutTradeNo(orderPay.getOutTradeNo());
            model.setRefundAmount(refundFee);
            model.setRefundReason("正常退款");
            model.setOutRequestNo(refundOrderNo);
            request.setBizModel(model);
            AlipayTradeRefundResponse response = alipayClient.certificateExecute(request);
            log.info("lotteryAliH5Recorver refund body:{}", response.getBody());
            if (response.isSuccess()) {
                //todo 如果成功,调用支付宝查询接口查询是否退款成功
                if (flag) {
                    cjLotteryRecordDao.updateStatusById(PrizeStatusEnum.yi_hui_shou.getCode(), record.getId());
                }
                //支付宝退款接口没有回调通知，更新订单状态
                this.createRefundOrder(orderPay, refundOrderNo, userId, refundFeeInFen);
                orderPayDao.updateStatusById(orderPay.getId(), PayStatusEnum.REFUND.getCode());
                notifyPayDao.updateStatusByOutTradeNo(orderPay.getOutTradeNo(), PayStatusEnum.REFUND.getCode());
                eventPublishService.addMoney(this, orderPay.getCustomerId(), refundFeeInFen, ScoreTypeEnum.JIAN);
            }
            return;
        } catch (Exception ex) {
            log.info("lotteryAliH5Recorver fail,userId:{},exception:", userId, ex);
        }
    }
    @Override
    public void saveCallbackData(TransactionConsumeData data) {
        String outTradeNo = data.getOutTradeNo();
        log.info("saveCallbackData data:{}",JSONObject.toJSON(data));
        CjOrderPay orderPay = orderPayDao.selectByOutTradeNo(outTradeNo);
        if (orderPay.getStatus() != PayStatusEnum.NO_PAY.getCode()) {
            log.info("saveCallbackData 支付回调，订单状态异常，orderPay:{},wxNotifyBean:{}", JSONObject.toJSON(orderPay), JSONObject.toJSON(data));
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
        log.info("wxRefundBack data:{}",JSONObject.toJSON(data));
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
        orderPay.setStatus(PayStatusEnum.NO_PAY.getCode());
        orderPay.setTradeType(payType.getCode());
        orderPay.setTotalFee(totalFee);
        orderPayDao.insertSelective(orderPay);
        return orderPay;
    }

    private static String intToFloat(int fee){

        DecimalFormat df = new DecimalFormat("0.00");
        return df.format((float)fee/100);//返回的是String类型
    }



}
