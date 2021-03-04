package com.cj.lottery.service;

import com.cj.lottery.domain.view.CjProductInfoVo;

import java.util.List;

public interface ProductInfoService {

    /**
     * 根据用户信息查询
     * @param status
     * @param userId
     * @return
     */
    List<CjProductInfoVo> queryProductByStatusAndUserId(Integer status,Integer userId);
}
