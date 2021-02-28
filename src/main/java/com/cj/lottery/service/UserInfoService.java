package com.cj.lottery.service;

import com.cj.lottery.domain.CjCustomerInfo;
import com.cj.lottery.domain.CjCustomerLogin;
import com.cj.lottery.domain.view.ConstumerAddressInfoVo;

import java.util.List;

public interface UserInfoService {

    List<ConstumerAddressInfoVo> queryAddressListByConstmerId(int constmerId);

    CjCustomerLogin queryLoginInfoByLoginPhone(String login);

    int updateUserAddress(ConstumerAddressInfoVo constumerAddressInfoVo);

    int deleteUserAddress(Integer id);

    CjCustomerInfo queryUserInfoByCustomerId(int coustmerId);

    /**
     * 获取用户最新的token
     *
     * @param loginAccount
     * @return
     */
    String queryLatestToken(String loginAccount);
}
