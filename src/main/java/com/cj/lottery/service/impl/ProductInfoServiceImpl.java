package com.cj.lottery.service.impl;

import com.alibaba.fastjson.JSON;
import com.cj.lottery.dao.CjLotteryRecordDao;
import com.cj.lottery.dao.CjProductInfoDao;
import com.cj.lottery.domain.CjLotteryRecord;
import com.cj.lottery.domain.view.CjProductInfoVo;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.PrizeStatusVo;
import com.cj.lottery.enums.PrizeStatusEnum;
import com.cj.lottery.service.ProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
        PrizeStatusVo prizeStatusVo = JSON.parseObject(JSON.toJSONString(keyMap), PrizeStatusVo.class);
        return CjResult.success(prizeStatusVo);
    }

    @Override
    public List<CjProductInfoVo> queryProductByStatusAndUserId(Integer status, Integer userId) {
        return null;
    }
}
