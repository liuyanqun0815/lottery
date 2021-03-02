package com.cj.lottery.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cj.lottery.dao.CjCustomerLoginDao;
import com.cj.lottery.dao.CjOrderPayDao;
import com.cj.lottery.domain.CjCustomerLogin;
import com.cj.lottery.domain.CjNotifyPay;
import com.cj.lottery.domain.CjOrderPay;
import com.cj.lottery.domain.pay.PayAmount;
import com.cj.lottery.domain.pay.Payer;
import com.cj.lottery.domain.pay.WxGzhOrderParam;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.enums.ErrorEnum;
import com.cj.lottery.enums.PayStatusEnum;
import com.cj.lottery.enums.PayTypeEnum;
import com.cj.lottery.service.OrderPayService;
import com.cj.lottery.util.HttpClientResult;
import com.cj.lottery.util.HttpClientUtils;
import com.cj.lottery.util.UuidUtils;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;


@Service
@Slf4j
public class OrderPayServiceImpl implements OrderPayService {

    @Value("${app.weixin.pay.appid}")
    private String appid;

    @Value("${app.weixin.pay.mchid}")
    private String mchid;

    @Value("${app.weixin.pay.notifyUrl}")
    private String notifyUrl;

    @Value("${app.weixin.pay.url}")
    private String gzhPayUrl;

    @Autowired
    private CjOrderPayDao orderPayDao;

    @Autowired
    private CjCustomerLoginDao cjCustomerLoginDao;

    private String description = "充值扭扭币";

    @Override
    public CjResult<String> createWxOrderPay(int customerId, int totalFee) {
        totalFee = totalFee * 100;
        CjCustomerLogin login = cjCustomerLoginDao.selectById(customerId);
        if (login == null) {
            return CjResult.fail(ErrorEnum.USERINFO_NOT_EXIST);
        }
        String openid = login.getLoginPhone();
        String out_trade_no = UuidUtils.getOrderNo();
        WxGzhOrderParam orderParam = this.buildOrderParam(customerId, totalFee, out_trade_no, openid);
        HttpClientResult data = HttpClientUtils.doPost(gzhPayUrl, null, BeanMap.create(orderParam), true);
        if (data.getCode() != 200) {
            return CjResult.fail(ErrorEnum.SYSTEM_ERROR);
        }
        String content = data.getContent();
        JSONObject object = JSONObject.parseObject(content);
        String prepay_id = object.getString("prepay_id");
        CjOrderPay cjOrderPay = this.buildOrderPayDO(customerId, totalFee, out_trade_no);
        orderPayDao.insertSelective(cjOrderPay);
        return CjResult.success(prepay_id);
    }

    @Override
    public CjResult<Void> wxOrderNotify() {
        return null;
    }

    @Override
    public CjResult<Void> savePaySuccess(WxPayOrderNotifyResult result) {

        CjNotifyPay pay = new CjNotifyPay();
        pay.setAppid(result.getAppid());
        pay.setMchId(result.getMchId());
//        pay.setCreateTime(result.gets());
        return null;
    }

    @Override
    public CjResult<Boolean> queryLatestOrderStatus(int customerId) {
        //查询用户最近一分钟一条订单
        Date date = DateUtils.addMinutes(new Date(), -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        String startTime = sdf.format(date);
        CjOrderPay cjOrderPay = orderPayDao.selectLatestOrder(customerId,startTime);
        return this.checkOrder(cjOrderPay);
    }

    @Override
    public CjResult<Boolean> queryOrderByUserIdAndOutTradeNo(int customerId, String outTradeNo) {
        CjOrderPay cjOrderPay = orderPayDao.selectByUserIdAndOutTradeNo(customerId, outTradeNo);
        return this.checkOrder(cjOrderPay);
    }

    private CjResult<Boolean> checkOrder(CjOrderPay cjOrderPay ){
        if (cjOrderPay == null){
            return CjResult.fail(ErrorEnum.NOT_ORDER);
        }
        if (cjOrderPay.getStatus() == PayStatusEnum.NO_PAY.getCode()){
            return CjResult.fail(ErrorEnum.NO_PAY);
        }
        if (cjOrderPay.getStatus() == PayStatusEnum.REFUND.getCode()){
            return CjResult.fail(ErrorEnum.REFUND);
        }
        if (cjOrderPay.getStatus() == PayStatusEnum.USED.getCode()){
            return CjResult.fail(ErrorEnum.USED);
        }
        if (cjOrderPay.getStatus() == PayStatusEnum.PAY.getCode()){
            return CjResult.success(true);
        }
        return CjResult.fail(ErrorEnum.SYSTEM_ERROR);
    }

    /**
     * 构建公众号支付参数
     *
     * @param customerId
     * @param totalFee
     * @param out_trade_no
     * @param openId
     * @return
     */
    private WxGzhOrderParam buildOrderParam(int customerId, int totalFee, String out_trade_no, String openId) {
        WxGzhOrderParam param = new WxGzhOrderParam();
        param.setAmount(PayAmount.builder().total(totalFee).build());
        param.setAppid(appid);
        param.setMchid(mchid);
        param.setDescription(description);
        param.setNotify_url(notifyUrl);
        param.setOut_trade_no(out_trade_no);
        param.setPayer(Payer.builder().openid(openId).build());
        return param;
    }

    /**
     * 构建订单表参数
     *
     * @param customerId
     * @param totalFee
     * @param out_trade_no
     * @return
     */
    private CjOrderPay buildOrderPayDO(int customerId, int totalFee, String out_trade_no) {
        CjOrderPay orderPay = new CjOrderPay();
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
