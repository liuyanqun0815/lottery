package com.cj.lottery.service;

import com.cj.lottery.domain.CjCustomerInfo;
import com.cj.lottery.domain.CjCustomerLogin;
import com.cj.lottery.domain.view.ConstumerAddressInfoVo;

import java.util.List;

public interface UserInfoService {

    List<ConstumerAddressInfoVo> queryAddressListByConstmerId(int constmerId);

    CjCustomerLogin queryLoginInfoByLoginPhone(String login);

<<<<<<< HEAD
    int updateUserAddress(ConstumerAddressInfoVo constumerAddressInfoVo);

    int deleteUserAddress(Integer id);
=======
    CjCustomerInfo queryUserInfoByCustomerId(int coustmerId);
>>>>>>> b6ff83c609995db1a010c87a223667b437d89982
}
