package com.cj.lottery.controller;

import com.cj.lottery.domain.CjOrderPay;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.PayMoneyRecordVo;
import com.cj.lottery.domain.view.PrizeStatusVo;
import com.cj.lottery.service.ProductInfoService;
import com.cj.lottery.util.ContextUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "奖品",description = "奖品相关接口")
@RestController
@Slf4j
@RequestMapping("api/prize")
public class ProductController {

    @Autowired
    private ProductInfoService productInfoService;

    @ApiOperation("奖品每个状态个数")
    @PostMapping("prize-status-num")
    public CjResult<PrizeStatusVo> prizeStatusNum() {
        return productInfoService.getPrizeStatusNum(ContextUtils.getUserId());
    }

}
