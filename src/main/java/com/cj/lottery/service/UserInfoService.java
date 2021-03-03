package com.cj.lottery.service;

import com.cj.lottery.domain.CjCustomerInfo;
import com.cj.lottery.domain.CjCustomerLogin;
import com.cj.lottery.domain.view.ConstumerAddressInfoVo;

import java.util.List;

public interface UserInfoService {

    List<ConstumerAddressInfoVo> queryAddressListByConstmerId(int constmerId);

    CjCustomerLogin queryLoginInfoByLoginPhone(String login);

    int saveOrupdateUserAddress(ConstumerAddressInfoVo constumerAddressInfoVo);

    int deleteUserAddress(Integer id);

    CjCustomerInfo queryUserInfoByCustomerId(int coustmerId);

    /**
     * 获取用户最新的token
     *
     * @param loginAccount
     * @return
     */
    String queryLatestToken(String loginAccount);

    /**
     * 首次登录，保存用户信息
     * @param loginMark
     * @param nickname
     * @param sex  默认男性
     * @param headimgurl
     * @return
     */
    String saveUserInfo(String loginMark, String nickname, Integer sex, String headimgurl);
}
