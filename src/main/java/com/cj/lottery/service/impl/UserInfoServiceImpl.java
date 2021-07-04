package com.cj.lottery.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cj.lottery.dao.*;
import com.cj.lottery.domain.*;
import com.cj.lottery.domain.manage.UserBaseInfo;
import com.cj.lottery.domain.manage.UserLotteryRecordVo;
import com.cj.lottery.domain.manage.UserPayRecordVo;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.ConstumerAddressInfoVo;
import com.cj.lottery.domain.view.PageView;
import com.cj.lottery.enums.SexEnum;
import com.cj.lottery.service.UserInfoService;
import com.cj.lottery.util.ContextUtils;
import com.cj.lottery.util.RandomValueUtils;
import com.cj.lottery.util.UuidUtils;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

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
    @Autowired
    private CjNotifyPayDao notifyPayDao;
    @Autowired
    private CjLotteryRecordDao lotteryRecordDao;
    @Autowired
    private CjMerchantDao merchantDao;

    @Override
    public List<ConstumerAddressInfoVo> queryAddressListByConstmerId(int constmerId) {
        List<CjCustomerAddress> cjCustomerAddresses = cjCustomerAddressDao.selectByCustmerId(constmerId);
        if(CollectionUtils.isEmpty(cjCustomerAddresses)){
            return Lists.emptyList();
        }
        return cjCustomerAddresses.stream().map(s->ConstumerAddressInfoVo.DoToVo(s)).collect(Collectors.toList());
    }

    @Override
    public CjCustomerLogin queryLoginInfoByLoginPhone(String login) {
        return customerLoginDao.selectByLoginPhone(login);
    }

    @Override
    public int saveOrupdateUserAddress(ConstumerAddressInfoVo addressInfoVo) {
        CjCustomerAddress cjCustomerAddress = ConstumerAddressInfoVo.VoToDo(addressInfoVo);
        int userId = ContextUtils.getUserId();
        cjCustomerAddress.setCustomerId(userId);
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
    public String queryLatestToken(String mobile, String channel,String ua) {
        CjCustomerLogin login = customerLoginDao.selectByLoginPhone(mobile);
        if (login == null){
            return saveUserInfo(mobile,RandomValueUtils.getUserNumberId(),SexEnum.BOY.getCode(),null,channel,ua);
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
    public String saveUserInfo(String loginMark, int numId, Integer sex, String headimgurl,String channel,String ua) {
        CjCustomerLogin login = new CjCustomerLogin();
        login.setLoginPhone(loginMark);
        customerLoginDao.insertSelective(login);
        CjCustomerInfo info = new CjCustomerInfo();
        info.setCustomerName(numId);
        info.setSex(sex);
        info.setHeadUrl(headimgurl);
        info.setCustomerId(login.getId());
        info.setChannel(channel);
        info.setUa(ua);
        info.setAccount(loginMark);
        customerInfoDao.insertSelective(info);

        String uniqueCode = UuidUtils.getUUid();
        CjCustomerLoginLog loginLog = new CjCustomerLoginLog();
        loginLog.setCustomerId(login.getId());
        loginLog.setUniqueCode(uniqueCode);
        cjCustomerLoginLogDao.insertSelective(loginLog);
        return uniqueCode;
    }

    @Override
    public CjResult<PageView> listUserBaseInfo(int currentPage, int pageSize, String account, String customerCode, String startTime, String endTime,String channel) {
        PageView pageView = new PageView();
        Page<UserBaseInfo> page = new Page<>(currentPage, pageSize);

        IPage<UserBaseInfo> userPage = customerInfoDao.selectBaseUserInfo(page,account, customerCode, startTime, endTime,channel);
        long total = userPage.getTotal();
        pageView.setSize(total);
        pageView.setModelList(userPage.getRecords());
        return CjResult.success(pageView);
    }

    @Override
    public CjResult<PageView> listUserPayRecord(int currentPage, int pageSize, String account, String customerCode, String startTime, String endTime, Integer status, String channel) {
        PageView pageView = new PageView();
        Page<UserPayRecordVo> page = new Page<>(currentPage, pageSize);
        IPage<UserPayRecordVo> pageVo = notifyPayDao.selectPayRecord(page,account,customerCode,startTime,endTime,channel,status);
        pageView.setSize(pageVo.getTotal());
        pageView.setModelList(pageVo.getRecords());
        return CjResult.success(pageView);
    }

    @Override
    public CjResult<PageView> listUserLotteryRecord(int currentPage, int pageSize, String account, String customerCode, String startTime, String endTime, Integer status, String channel) {
        PageView pageView = new PageView();
        Page<UserLotteryRecordVo> page = new Page<>(currentPage, pageSize);
        IPage<UserLotteryRecordVo> pageVo = lotteryRecordDao.selectLotterRecord(page,account,customerCode,startTime,endTime,channel,status);
        pageView.setSize(pageVo.getTotal());
        pageView.setModelList(pageVo.getRecords());
        return CjResult.success(pageView);
    }

    @Override
    public CjResult<PageView> listChannelRecord(int currentPage, int pageSize, String channel, String channelName) {
        PageView pageView = new PageView();
        Page<CjMerchant> page = new Page<>(currentPage, pageSize);
        IPage<CjMerchant> pageVo = merchantDao.selectChannelRecord(page,channel,channelName);
        pageView.setSize(pageVo.getTotal());
        pageView.setModelList(pageVo.getRecords());
        return CjResult.success(pageView);
    }

    @Override
    public CjResult<Boolean> login(String account, String password) {
        CjCustomerInfo info = customerLoginDao.selectByAccountAndPassword(account,password);
        return info == null ?CjResult.success(false):CjResult.success(true);
    }
}
