package com.cj.lottery.service.impl;

import com.cj.lottery.dao.CjCustomerAddressDao;
import com.cj.lottery.dao.CjCustomerInfoDao;
import com.cj.lottery.dao.CjCustomerLoginDao;
import com.cj.lottery.dao.CjCustomerLoginLogDao;
import com.cj.lottery.domain.CjCustomerAddress;
import com.cj.lottery.domain.CjCustomerInfo;
import com.cj.lottery.domain.CjCustomerLogin;
import com.cj.lottery.domain.CjCustomerLoginLog;
import com.cj.lottery.domain.view.ConstumerAddressInfoVo;
import com.cj.lottery.enums.SexEnum;
import com.cj.lottery.mapper.ConstumerAddressInfoMapper;
import com.cj.lottery.service.UserInfoService;
import com.cj.lottery.util.RandomValueUtils;
import com.cj.lottery.util.UuidUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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
    @Autowired
    private CjCustomerLoginDao customerLoginDao;
    @Autowired
    private CjCustomerLoginLogDao cjCustomerLoginLogDao;

    @Override
    public List<ConstumerAddressInfoVo> queryAddressListByConstmerId(int constmerId) {
        List<CjCustomerAddress> cjCustomerAddresses = cjCustomerAddressDao.selectByCustmerId(constmerId);
        if(CollectionUtils.isEmpty(cjCustomerAddresses)){
            return null;
        }
        return cjCustomerAddresses.stream().map(s->ConstumerAddressInfoVo.DoToVo(s)).collect(Collectors.toList());
    }

    @Override
    public CjCustomerLogin queryLoginInfoByLoginPhone(String login) {
        return customerLoginDao.selectByLoginPhone(login);
    }

    @Override
    public int saveOrupdateUserAddress(ConstumerAddressInfoVo constumerAddressInfoVo) {
        CjCustomerAddress cjCustomerAddress = ConstumerAddressInfoMapper.INSTANCE.toDo(constumerAddressInfoVo);
        if(ObjectUtils.isEmpty(cjCustomerAddress.getId())){
            return cjCustomerAddressDao.insertSelective(cjCustomerAddress);
        }
        return cjCustomerAddressDao.updateByPrimaryKeySelective(cjCustomerAddress);
    }


    @Override
    public int deleteUserAddress(Integer id) {
        return cjCustomerAddressDao.deleteByPrimaryKey(id);
    }

    @Override
    public CjCustomerInfo queryUserInfoByCustomerId(int coustmerId) {
        return customerInfoDao.selectByCustomerId(coustmerId);
    }


    @Override
    public String queryLatestToken(String loginAccount) {
        CjCustomerLogin login = customerLoginDao.selectByLoginPhone(loginAccount);
        if (login == null){
            return saveUserInfo(loginAccount,RandomValueUtils.getChineseName(SexEnum.BOY.getCode()),SexEnum.BOY.getCode(),null);
        }
        String token = cjCustomerLoginLogDao.selectTokenByCustomerId(login.getId());
        if (ObjectUtils.isEmpty(token)) {
            token = UuidUtils.getUUid();
            CjCustomerLoginLog loginLog = new CjCustomerLoginLog();
            loginLog.setCustomerId(login.getId());
            loginLog.setUniqueCode(token);
            cjCustomerLoginLogDao.insertSelective(loginLog);
        }
        return token;

    }

    @Override
    public String saveUserInfo(String loginMark, String nickname, Integer sex, String headimgurl) {
        CjCustomerLogin login = new CjCustomerLogin();
        login.setLoginPhone(loginMark);
        customerLoginDao.insertSelective(login);
        CjCustomerInfo info = new CjCustomerInfo();
        info.setCustomerName(nickname);
        info.setSex(sex);
        info.setHeadUrl(headimgurl);
        info.setCustomerId(login.getId());
        customerInfoDao.insertSelective(info);
        String uniqueCode = UuidUtils.getUUid();
        CjCustomerLoginLog loginLog = new CjCustomerLoginLog();
        loginLog.setCustomerId(login.getId());
        loginLog.setUniqueCode(uniqueCode);
        cjCustomerLoginLogDao.insertSelective(loginLog);
        return uniqueCode;
    }
}
