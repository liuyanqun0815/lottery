package com.cj.lottery.controller;

import com.cj.lottery.constant.ContextCons;
import com.cj.lottery.domain.view.*;
import com.cj.lottery.enums.ErrorEnum;
import com.cj.lottery.enums.MaidianTypeEnum;
import com.cj.lottery.service.*;
import com.cj.lottery.util.ContextUtils;
import com.cj.lottery.util.IpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @Autowired
    private MaidianService maidianService;


    @ApiOperation("获取活动列表")
    @PostMapping("list-activity")
    public CjResult<PageView> listActity(HttpServletRequest request,
                                         @RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         @RequestParam(required = false)String channel) {
        String token = request.getHeader(ContextCons.TOKEN);
        Integer userId = null;
        if (com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isNotEmpty(token)) {
            userId = customerLoginService.getUserIdByToken(token);
        }
        String ipAddr = IpUtil.getIpAddr(request);
        //埋点
        maidianService.saveRecord(MaidianTypeEnum.SHOU_YE,userId,ipAddr,null,channel);
        PageView pageView = lotteryActivityService.queryActivityListByPage(pageIndex, pageSize,userId);
        return CjResult.success(pageView);
    }

    @ApiOperation("获取活动详情")
    @PostMapping("activity-info")
    public CjResult<LotteryActivityInfoVo> actityInfo(HttpServletRequest request,
                                                      @RequestParam String activityCode,
                                                      @RequestParam(required = false)String channel){
        int userId = ContextUtils.getUserId();
        String ipAddr = IpUtil.getIpAddr(request);
        maidianService.saveRecord(MaidianTypeEnum.ACTIVITY_INFO,userId,ipAddr,activityCode,channel);
        return lotteryActivityService.queryActivityDetailsByPage( userId,activityCode);
    }

    @ApiOperation("获取弹幕列表")
    @PostMapping("list-danmu")
    public CjResult<List<CjLotteryMaopaoVo>> listDanmu(@RequestParam(value = "activityCode") String activityCode){
        //通过固定配置信息返回弹幕
        return luckDrawLotteryService.getAwardwinningUserInfo(activityCode);
    }

    @ApiOperation("新人活动接口")
    @PostMapping("/new-people-activitie")
    public CjResult<NewPepoleActivityVo> newPeopleActivitie(HttpServletRequest request){
        //查询是否登录
        Integer userId = null;
        boolean loginFlag = false;
        String token = request.getHeader(ContextCons.TOKEN);
        if (!ObjectUtils.isEmpty(token)) {
             userId = customerLoginService.getUserIdByToken(token);
            if (userId != null) {
                loginFlag = true;
            }
        }
        return luckDrawLotteryService.newPeopleActivities(loginFlag,userId);
    }


}


