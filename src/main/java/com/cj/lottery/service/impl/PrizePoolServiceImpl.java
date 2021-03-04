package com.cj.lottery.service.impl;

import com.cj.lottery.dao.CjLotteryActivityDao;
import com.cj.lottery.dao.CjPrizePoolDao;
import com.cj.lottery.domain.CjLotteryActivity;
import com.cj.lottery.domain.CjPrizePool;
import com.cj.lottery.service.PrizePoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class PrizePoolServiceImpl implements PrizePoolService {

    @Autowired
    private CjPrizePoolDao prizePoolDao;
    @Autowired
    private CjLotteryActivityDao lotteryActivityDao;

    @Override
    public List<CjPrizePool> getProductByActivityCode(int acitvityId) {
        return prizePoolDao.selectProductByActivityId(acitvityId);
    }

    @Override
    public void queryRealProduct(String activityCode) {
        CjLotteryActivity cjLotteryActivity = lotteryActivityDao.selectActivityByCode(activityCode);
        if (cjLotteryActivity == null){
            return;
        }
        int acitvityId = cjLotteryActivity.getId();
        List<CjPrizePool> productList = this.getProductByActivityCode(acitvityId);
        //过滤掉，奖品池中数量为0的商品
        productList = productList.stream().filter(s->s.getProductLatestNum()>0).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(productList)){
            return;
        }
    }
}
