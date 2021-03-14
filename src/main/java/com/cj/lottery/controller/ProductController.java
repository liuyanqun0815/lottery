package com.cj.lottery.controller;

import com.cj.lottery.domain.CjOrderPay;
import com.cj.lottery.domain.view.CjProductInfoVo;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.PayMoneyRecordVo;
import com.cj.lottery.domain.view.PrizeStatusVo;
import com.cj.lottery.enums.ErrorEnum;
import com.cj.lottery.enums.PrizeStatusEnum;
import com.cj.lottery.service.ProductInfoService;
import com.cj.lottery.util.ContextUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "奖品", description = "奖品相关接口")
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

    @ApiOperation("奖品列表")
    @PostMapping("list-prize")
    public CjResult<List<CjProductInfoVo>> listPrize(@RequestParam PrizeStatusEnum status) {
        return CjResult.success(productInfoService.queryProductByStatusAndUserId(status, ContextUtils.getUserId()));
    }

    @ApiOperation("我要发货")
    @PostMapping("send-prize")
    public CjResult<Void> sendPrize(@ApiParam("奖品记录唯一标识") @RequestParam List<Integer> idList) {
        if (!CollectionUtils.isEmpty(idList) && idList.size()>4){
            return CjResult.fail(ErrorEnum.PRIZE_PAY);
        }
        return productInfoService.sendGoods(idList, ContextUtils.getUserId());
    }

}
