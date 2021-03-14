package com.cj.lottery.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cj.lottery.dao.CjLotteryRecordDao;
import com.cj.lottery.dao.CjProductInfoDao;
import com.cj.lottery.domain.CjLotteryRecord;
import com.cj.lottery.domain.CjProductInfo;
import com.cj.lottery.domain.view.CjProductInfoVo;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.PrizeStatusVo;
import com.cj.lottery.enums.ErrorEnum;
import com.cj.lottery.enums.PrizeStatusEnum;
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
    public CjResult<Void> sendGoods(List<Integer> idList, int userId) {
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
        }
        return CjResult.success();
    }

    private List<CjProductInfoVo> toCjProductInfoVos(List<CjLotteryRecord> cjLotteryRecords) {
        List<CjProductInfoVo> infoVoList = Lists.newArrayList();
        if(!CollectionUtils.isEmpty(cjLotteryRecords)){
            List<Integer> productIds = cjLotteryRecords.stream().map(record -> record.getProductId()).collect(Collectors.toList());
            List<CjProductInfo> cjProductInfos = productInfoDao.selectByIds(productIds);
            Map<Integer, CjProductInfo> productidMap = cjProductInfos.stream().collect(Collectors.toMap(CjProductInfo::getId, Function.identity()));
            infoVoList = cjLotteryRecords.stream().map(s->CjProductInfoVo.DoToVo(s,productidMap)).collect(Collectors.toList());
        }
        return infoVoList;
    }
}
