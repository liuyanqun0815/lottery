package com.cj.lottery.controller;

import com.cj.lottery.domain.CjLotteryActivityImg;
import com.cj.lottery.domain.view.*;
import com.cj.lottery.service.CustomerLoginService;
import com.cj.lottery.service.LotteryActivityService;
import com.cj.lottery.service.LuckDrawLotteryService;
import com.cj.lottery.service.PrizePoolService;
import com.cj.lottery.util.ContextUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuyanqun
 */
@Api(value = "活动",description = "活动接口")
@Slf4j
@RestController
@RequestMapping("api/cj/activity")
public class lotteryActivityController {

    @Autowired
    LotteryActivityService lotteryActivityService;
    @Autowired
    PrizePoolService prizePoolService;
    @Autowired
    private LuckDrawLotteryService luckDrawLotteryService;
    @Autowired
    private CustomerLoginService customerLoginService;


    @ApiOperation("获取活动列表")
    @PostMapping("list-activity")
    public CjResult<PageView> listActity(@RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageView pageView = lotteryActivityService.queryActivityListByPage(pageIndex, pageSize);
        return CjResult.success(pageView);
    }

    @ApiOperation("获取活动详情")
    @PostMapping("activity-info")
    public CjResult<LotteryActivityInfoVo> actityInfo(@RequestParam(value = "activityCode") String activityCode){
        int userId = ContextUtils.getUserId();
        return lotteryActivityService.queryActivityDetailsByPage( userId,activityCode);
    }

    @ApiOperation("获取弹幕列表")
    @PostMapping("list-danmu")
    public CjResult<List<String>> listDanmu(@RequestParam(value = "activityCode") String activityCode){
        //通过固定配置信息返回弹幕
        List<String> danmuList = new ArrayList();
        danmuList.add("恭喜凤凰：抽到小米蓝牙耳机");
        danmuList.add("恭喜大仙：抽到apple耳机");
        return CjResult.success(danmuList);
    }

    @ApiOperation("新人活动接口")
    @GetMapping("/new-people-activitie")
    public CjResult<NewPepoleActivityVo> newPeopleActivitie(HttpServletRequest request){
        //查询是否登录
        boolean loginFlag = false;
        String token  = request.getHeader("token");
        Integer userId = customerLoginService.getUserIdByToken(token);
        if (userId != null){
            loginFlag = true;
        }
        return luckDrawLotteryService.newPeopleActivities(loginFlag,userId);
    }

}
