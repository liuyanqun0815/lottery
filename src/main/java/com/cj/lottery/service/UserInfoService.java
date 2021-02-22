package com.cj.lottery.service;

import com.cj.lottery.domain.view.ConstumerAddressInfoVo;

import java.util.List;

public interface UserInfoService {

    List<ConstumerAddressInfoVo> queryAddressListByConstmerId(int constmerId);
}
