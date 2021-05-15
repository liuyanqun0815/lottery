package com.cj.lottery.service.impl;

import com.cj.lottery.dao.CjMerchantDao;
import com.cj.lottery.domain.CjMerchant;
import com.cj.lottery.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private CjMerchantDao merchantDao;

    @Override
    public CjMerchant queryMerchant() {
        List<CjMerchant> cjMerchants = merchantDao.selelctAllMerchant();
        if (CollectionUtils.isEmpty(cjMerchants)){
            return null;
        }
        return cjMerchants.get(0);
    }

    @Override
    public int saveMerchant(String channel) {
        return merchantDao.insert(channel);
    }


}
