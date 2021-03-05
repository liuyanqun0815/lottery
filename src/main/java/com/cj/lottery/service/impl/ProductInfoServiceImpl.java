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
        System.out.println(JSONObject.toJSON(prizeStatusVo));
        return CjResult.success(prizeStatusVo);
    }

    @Override
    public List<CjProductInfoVo> queryProductByStatusAndUserId(Integer status, Integer custmerId) {
        List<CjProductInfoVo> infoVoList = Lists.newArrayList();
        List<CjLotteryRecord> cjLotteryRecords = lotteryRecordDao.selectRecordByConsumerIdAndStatus(status, custmerId);
        if(!CollectionUtils.isEmpty(cjLotteryRecords)){
            List<Integer> productIds = cjLotteryRecords.stream().map(record -> record.getProductId()).collect(Collectors.toList());
            List<CjProductInfo> cjProductInfos = productInfoDao.selectByIds(productIds);
            infoVoList = CjProductInfoMapper.INSTANCE.toVos(cjProductInfos);
        }
        return infoVoList;
    }
}
