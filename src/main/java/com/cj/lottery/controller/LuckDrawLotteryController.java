package com.cj.lottery.controller;


import com.cj.lottery.constant.ContextCons;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.LotteryResultVo;
import com.cj.lottery.service.LuckDrawLotteryService;
import com.cj.lottery.service.PrizePoolService;
import com.cj.lottery.util.ContextUtils;
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
    @Autowired
    private LuckDrawLotteryService luckDrawLotteryService;

    @ApiOperation("点击抽奖结果")
    @PostMapping("click-prize")
    public CjResult<Object> clickPrize(@RequestParam(value = "activityCode") String activityCode,
                                                @ApiParam("是否试玩")@RequestParam(value = "test")boolean test) {
        if(test){
            return CjResult.success(null);
        }
        int userId = ContextUtils.getUserId();
        boolean authority = luckDrawLotteryService.checkAuthority(userId, activityCode);
        if(!authority){
            return CjResult.fail("只有新用户可以参加此活动");
        }

        prizePoolService.queryRealProduct(activityCode);
        return CjResult.success(null);
    }




}
