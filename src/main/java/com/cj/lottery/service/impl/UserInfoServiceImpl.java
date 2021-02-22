package com.cj.lottery.service.impl;

import com.cj.lottery.dao.CjCustomerAddressDao;
import com.cj.lottery.dao.CjCustomerInfoDao;
import com.cj.lottery.domain.CjCustomerAddress;
import com.cj.lottery.domain.CjCustomerInfo;
import com.cj.lottery.domain.view.ConstumerAddressInfoVo;
import com.cj.lottery.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private CjCustomerAddressDao cjCustomerAddressDao;
    @Autowired
    private CjCustomerInfoDao customerInfoDao;


    @Override
    public List<ConstumerAddressInfoVo> queryAddressListByConstmerId(int constmerId) {
        List<CjCustomerAddress> cjCustomerAddresses = cjCustomerAddressDao.selectByCustmerId(constmerId);
        if(CollectionUtils.isEmpty(cjCustomerAddresses)){
            return null;
        }
        return cjCustomerAddresses.stream().map(s->ConstumerAddressInfoVo.DoToVo(s)).collect(Collectors.toList());
    }
}
