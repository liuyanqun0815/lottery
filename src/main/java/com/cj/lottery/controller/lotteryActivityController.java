package com.cj.lottery.controller;

import com.cj.lottery.domain.CjLotteryActivityImg;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.LotteryActivityInfoVo;
import com.cj.lottery.domain.view.LotteryResultVo;
import com.cj.lottery.domain.view.PageView;
import com.cj.lottery.service.LotteryActivityService;
import com.cj.lottery.service.PrizePoolService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuyanqun
 */
@Slf4j
@RestController
@RequestMapping("api/cj/activity")
public class lotteryActivityController {

    @Autowired
    LotteryActivityService lotteryActivityService;
    @Autowired
    PrizePoolService prizePoolService;

    @ApiOperation("获取活动列表")
    @PostMapping("list-activity")
    public CjResult<PageView> listActity(@RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageView pageView = lotteryActivityService.queryActivityListByPage(pageIndex, pageSize);
        return CjResult.success(pageView);
    }

    @ApiOperation("获取活动详情")
    @PostMapping("activity-info")
    public CjResult<LotteryActivityInfoVo> actityInfo(@RequestParam(value = "activityCode") String activityCode,
                                                      @RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
                                                      @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        LotteryActivityInfoVo lotteryActivityInfoVo = lotteryActivityService.queryActivityDetailsByPage(activityCode, pageIndex, pageSize);
        return CjResult.success(lotteryActivityInfoVo);
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

}
