package com.cj.lottery.service;

import com.cj.lottery.domain.CjCustomerInfo;
import com.cj.lottery.domain.CjCustomerLogin;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.ConstumerAddressInfoVo;
import com.cj.lottery.domain.view.PageView;

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
     *
     * @param loginAccount
     * @param s
     * @param mobile
     * @return
     */
    String queryLatestToken(String mobile, String channel, String ua);

    /**
     * 首次登录，保存用户信息
     * @param loginMark
     * @param nickname
     * @param sex  默认男性
     * @param headimgurl
     * @return
     */
    String saveUserInfo(String loginMark, int nickname, Integer sex, String headimgurl,String channel,String ua);

    CjResult<PageView> listUserBaseInfo(int currentPage, int pageSize, String account,
                                        String customerCode, String startTime, String endTime,String channel);

    CjResult<PageView> listUserPayRecord(int currentPage, int pageSize, String account, String customerCode, String startTime, String endTime, Integer status, String channel);

    CjResult<PageView> listUserLotteryRecord(int currentPage, int pageSize, String account, String customerCode, String startTime, String endTime, Integer status, String channel);

    CjResult<PageView> listChannelRecord(int currentPage, int pageSize, String channel, String channelName);

    CjResult<Boolean> login(String account, String password);
}
