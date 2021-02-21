package com.cj.lottery.service;

import com.cj.lottery.domain.CjPrizePool;

import java.util.List;

public interface PrizePoolService {

    /**通过活动code获取下面所有的商品
     * @param acitvityId
     * @return
     */
    List<CjPrizePool> getProductByActivityCode(int acitvityId);

    void queryRealProduct(String activityCode);
}
