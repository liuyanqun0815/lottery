package com.cj.lottery.service;

import com.cj.lottery.domain.CjProductInfo;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.PrizeStatusVo;
import com.cj.lottery.enums.PrizeStatusEnum;
import com.cj.lottery.domain.view.CjProductInfoVo;

import java.util.List;

public interface ProductInfoService {


    CjResult<PrizeStatusVo> getPrizeStatusNum(int custmerId);


    /**
     * 根据用户信息查询
     * @param status
     * @param userId
     * @return
     */
    List<CjProductInfoVo> queryProductByStatusAndUserId(PrizeStatusEnum status,Integer userId);

    CjResult<Void> sendGoods(List<Integer> idList, int userId);
}
