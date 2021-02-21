package com.cj.lottery.service.impl;

import com.cj.lottery.dao.CjCustomerLoginLogDao;
import com.cj.lottery.service.CustomerLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class CustomerLoginServiceImpl implements CustomerLoginService {

    @Autowired
    private CjCustomerLoginLogDao cjCustomerLoginLogDao;

    @Override
    public Integer getUserIdByToken(String token) {
        if (ObjectUtils.isEmpty(token)){
            return null;
        }
        return cjCustomerLoginLogDao.selectUserIdByToken(token);
    }
}
