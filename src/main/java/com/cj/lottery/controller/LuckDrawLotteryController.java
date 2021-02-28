package com.cj.lottery.controller;


import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.LotteryResultVo;
import com.cj.lottery.service.PrizePoolService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/cj/draw")
public class LuckDrawLotteryController {

    @Autowired
    private PrizePoolService prizePoolService;

    @ApiOperation("点击抽奖结果")
    @PostMapping("click-prize")
    public CjResult<LotteryResultVo> clickPrize(@RequestParam(value = "activityCode") String activityCode,
                                                @ApiParam("是否试玩")@RequestParam(value = "test",required = true)boolean test) {
        prizePoolService.queryRealProduct(activityCode);
        return CjResult.success(null);
    }




}
