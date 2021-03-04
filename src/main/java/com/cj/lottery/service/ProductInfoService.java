package com.cj.lottery.service;

import com.cj.lottery.domain.CjProductInfo;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.PrizeStatusVo;
import com.cj.lottery.enums.PrizeStatusEnum;

import java.util.List;

public interface ProductInfoService {


    CjResult<PrizeStatusVo> getPrizeStatusNum(int custmerId);


}
