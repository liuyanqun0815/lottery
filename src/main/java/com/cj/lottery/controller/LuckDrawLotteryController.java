package com.cj.lottery.controller;


import com.cj.lottery.constant.ContextCons;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.LotteryData;
import com.cj.lottery.domain.view.LotteryResultVo;
import com.cj.lottery.enums.ErrorEnum;
import com.cj.lottery.service.CustomerLoginService;
import com.cj.lottery.service.LuckDrawLotteryService;
import com.cj.lottery.service.PrizePoolService;
import com.cj.lottery.util.ContextUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(value = "抽奖", description = "抽奖接口")
@Slf4j
@RestController
@RequestMapping("api/cj/draw")
public class LuckDrawLotteryController {

    @Autowired
    private PrizePoolService prizePoolService;
    @Autowired
    private LuckDrawLotteryService luckDrawLotteryService;
    @Autowired
    private CustomerLoginService customerLoginService;

    @ApiOperation("点击抽奖结果")
    @PostMapping("click-prize")
    public CjResult<LotteryData> clickPrize(HttpServletRequest request,
                                            @RequestParam(value = "activityCode") String activityCode,
                                            @ApiParam("是否试玩") @RequestParam(value = "test") boolean test) {
        int userId = ContextUtils.getUserId();
        if (!test) {
            String token = request.getHeader(ContextCons.TOKEN);
            if (ObjectUtils.isEmpty(token)) {
                return CjResult.fail(ErrorEnum.NOT_TOKEN);
            }
            userId = customerLoginService.getUserIdByToken(token);
        }
        return luckDrawLotteryService.clickLottery(userId, activityCode, test);
    }


}
