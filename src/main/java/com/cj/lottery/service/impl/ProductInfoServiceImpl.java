package com.cj.lottery.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cj.lottery.dao.*;
import com.cj.lottery.domain.*;
import com.cj.lottery.domain.view.CjProductInfoVo;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.PrizeStatusVo;
import com.cj.lottery.enums.ErrorEnum;
import com.cj.lottery.enums.PrizeStatusEnum;
import com.cj.lottery.event.EventPublishService;
import com.cj.lottery.mapper.CjProductInfoMapper;
import com.cj.lottery.service.ProductInfoService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductInfoServiceImpl implements ProductInfoService {
    @Autowired
    private CjProductInfoDao productInfoDao;
    @Autowired
    private CjLotteryRecordDao lotteryRecordDao;
    @Autowired
    private CjCustomerAddressDao customerAddressDao;
    @Autowired
    private CjSendProductDao sendProductDao;
    @Autowired
    private EventPublishService eventPublishService;
    @Autowired
    private CjLotteryActivityDao lotteryActivityDao;


    @Override
    public CjResult<PrizeStatusVo> getPrizeStatusNum(int custmerId) {
        HashMap<Integer, HashMap<Integer, Long>> prizeStatuNum = lotteryRecordDao.getPrizeStatuNum(custmerId);
        Map<String, Integer> keyMap = new HashMap<>();
        if (prizeStatuNum != null && prizeStatuNum.size() > 0) {
            Iterator<Map.Entry<Integer, HashMap<Integer, Long>>> iterator = prizeStatuNum.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, HashMap<Integer, Long>> next = iterator.next();
                HashMap<Integer, Long> map = next.getValue();
                Integer key = Integer.valueOf(map.get("statusKey") + "");
                Integer value = Integer.valueOf(map.get("statusNum") + "");
                keyMap.put(PrizeStatusEnum.parse(key).getFiled(), value);
            }
        }
        if (keyMap.size() == 0) {
            return CjResult.success(new PrizeStatusVo());
        }
        PrizeStatusVo prizeStatusVo = JSON.parseObject(JSONObject.toJSONString(keyMap), PrizeStatusVo.class);
        return CjResult.success(prizeStatusVo);
    }

    @Override
    public List<CjProductInfoVo> queryProductByStatusAndUserId(PrizeStatusEnum status, Integer custmerId) {
        //TODO--分页
        List<CjLotteryRecord> cjLotteryRecords = lotteryRecordDao.selectRecordByConsumerIdAndStatus(status.getCode(), custmerId);
        return toCjProductInfoVos(cjLotteryRecords);
    }

    @Override
    public CjResult<Void> sendGoods(List<Integer> idList, int userId,boolean postageFlag) {
        List<CjCustomerAddress> cjCustomerAddresses = customerAddressDao.selectByCustmerId(userId);
        if (CollectionUtils.isEmpty(cjCustomerAddresses)){
            return CjResult.fail(ErrorEnum.USER_ADDRESS_NOT);
        }
        List<CjLotteryRecord> cjLotteryRecords = lotteryRecordDao.selectByIdList(idList);
        List<CjLotteryRecord> errList = cjLotteryRecords.stream().filter(s -> s.getStatus() != PrizeStatusEnum.dai_fa_huo.getCode()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(errList)){
            return CjResult.fail(ErrorEnum.PRIZE_STATUS_ERROR);
        }
        boolean present = cjLotteryRecords.stream().filter(s -> s.getCustomerId() != userId).findFirst().isPresent();
        if (present){
            return CjResult.fail(ErrorEnum.PRIZE_BELONG);
        }
        for(CjLotteryRecord record : cjLotteryRecords) {
            lotteryRecordDao.updateStatusById(PrizeStatusEnum.yi_fa_huo.getCode(),record.getId());
            //记录到发货记录表中,todo 目前支持一个地址，默认查询地址列表第一个
            this.saveSendRecord(cjCustomerAddresses.get(0).getId(),record.getProductId(),userId,postageFlag);
        }
        return CjResult.success();
    }

    private void saveSendRecord(Integer addressId,Integer productId,Integer userId,boolean postageFlag){
        CjSendProduct sendProduct = new CjSendProduct();
        sendProduct.setAddressId(addressId);
        sendProduct.setCustomerId(userId);
        sendProduct.setProductId(productId);
        sendProduct.setPostageFlag(postageFlag?1:0);
        sendProduct.setStatus(1);
        sendProductDao.insertSelective(sendProduct);
    }

    private List<CjProductInfoVo> toCjProductInfoVos(List<CjLotteryRecord> cjLotteryRecords) {
        List<CjProductInfoVo> infoVoList = Lists.newArrayList();
        if(!CollectionUtils.isEmpty(cjLotteryRecords)){
            //倒叙  排序
            cjLotteryRecords = cjLotteryRecords.stream().sorted((o1, o2) -> o2.getId().compareTo(o1.getId())).collect(Collectors.toList());;
            List<Integer> productIds = cjLotteryRecords.stream().map(record -> record.getProductId()).collect(Collectors.toList());
            List<CjProductInfo> cjProductInfos = productInfoDao.selectByIds(productIds);
            List<Integer> activityidList = cjLotteryRecords.stream().map(CjLotteryRecord::getActivityId).collect(Collectors.toList());
            List<CjLotteryActivity> activities = lotteryActivityDao.selectByIdList(activityidList);
            Map<Integer, CjLotteryActivity> activitMap = activities.stream().collect(Collectors.toMap(CjLotteryActivity::getId, Function.identity()));
            cjLotteryRecords.stream().forEach(s->{
                s.setCallbackRate(activitMap.get(s.getActivityId()).getActivityRate());
                s.setTotalFee(activitMap.get(s.getActivityId()).getConsumerMoney());
            });
            Map<Integer, CjProductInfo> productidMap = cjProductInfos.stream().collect(Collectors.toMap(CjProductInfo::getId, Function.identity()));
            infoVoList = cjLotteryRecords.stream().map(s->CjProductInfoVo.DoToVo(s,productidMap)).collect(Collectors.toList());
        }
        return infoVoList;
    }

}
