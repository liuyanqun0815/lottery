package com.cj.lottery.service.impl;

import com.cj.lottery.dao.simple.*;
import com.cj.lottery.domain.simple.*;
import com.cj.lottery.enums.PayStatusEnum;
import com.cj.lottery.service.SimpleActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class SimpleActivityServiceImpl implements SimpleActivityService {


    @Autowired
    private CjSimpleLotteryActivityDao cjSimpleLotteryActivityDao;
    @Autowired
    private CjSimpleProductInfoDao cjSimpleProductInfoDao;
    @Autowired
    private CjSimpleOrderPayDao cjSimpleOrderPayDao;
    @Autowired
    private CjSimpleCustomerInfoDao cjSimpleCustomerInfoDao;
    @Autowired
    private CjSimpleSendProductDao cjSimpleSendProductDao;
    @Autowired
    private CjSimpleMaidianRecordDao cjSimpleMaidianRecordDao;

    @Override
    public List<CjSimpleLotteryActivity> queryActivityList() {

        return cjSimpleLotteryActivityDao.selectAll();
    }

    @Override
    public List<CjSimpleProductInfo> queryProductByActivityIds(List<Integer> activityIdList) {
        return cjSimpleProductInfoDao.selectProductByActivityIds(activityIdList);
    }

    @Override
    public CjSimpleOrderPay queryOrderPay(String outTradeNo) {
        return cjSimpleOrderPayDao.selectByOutTradeNo(outTradeNo);

    }

    @Override
    public CjSimpleLotteryActivity queryActivityByCode(String activityCode) {
        return cjSimpleLotteryActivityDao.selectByCode(activityCode);
    }

    @Override
    public void sendPrize( String name, String mobile, String address, String addressInfo, String channel,String outTradeNo, Integer productId) {
        CjSimpleCustomerInfo info = new CjSimpleCustomerInfo();
        info.setChannel(channel);
        info.setMobile(mobile);
        info.setName(name);
        cjSimpleCustomerInfoDao.insertSelective(info);

        CjSimpleSendProduct product = new CjSimpleSendProduct();
        product.setAddress(address);
        product.setAddressInfo(addressInfo);
        product.setChannel(channel);
        product.setMobile(mobile);
        product.setName(name);
        product.setProductId(productId);
        product.setOutTradeNo(outTradeNo);
        cjSimpleSendProductDao.insertSelective(product);

        //更新订单状态
       int i = cjSimpleOrderPayDao.updateOrderStatusByOutradeNo(PayStatusEnum.USED.getCode(),outTradeNo);
    }

    @Override
    public CjSimpleProductInfo queryProductByCode(String productCode) {
        return cjSimpleProductInfoDao.selectByCode(productCode);
    }

    @Override
    public void buriedPoints(Integer id, int type, String ip, String channel, String ua) {
        CjSimpleMaidianRecord record = new CjSimpleMaidianRecord();
        record.setActivityId(id);
        record.setChannel(channel);
        record.setFunctionType(type);
        record.setIp(ip);
        record.setUa(ua);
        cjSimpleMaidianRecordDao.insertSelective(record);
    }

    @Override
    public void paySuccess(String outTradeNo) {
        cjSimpleOrderPayDao.updateOrderStatusByOutradeNo(PayStatusEnum.PAY.getCode(),outTradeNo);
    }
}
