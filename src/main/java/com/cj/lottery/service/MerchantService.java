package com.cj.lottery.service;

import com.cj.lottery.domain.CjMerchant;

public interface MerchantService {

    /**
     * 获取一个可以用的商户信息，
     * 暂时按照正序获取第一个可用的
     * @return
     */
    CjMerchant queryMerchant();

    int saveMerchant(String channel);
}
