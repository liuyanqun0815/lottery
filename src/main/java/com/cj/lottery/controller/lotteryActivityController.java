package com.cj.lottery.controller;

import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.LotteryActivityInfoVo;
import com.cj.lottery.domain.view.PageView;
import com.cj.lottery.service.LotteryActivityService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuyanqun
 */
@Slf4j
@RestController
@RequestMapping("api/cj/activity")
public class lotteryActivityController {

    @Autowired
    LotteryActivityService lotteryActivityService;

    @ApiOperation("获取活动列表")
    @PostMapping("list-activity")
    public CjResult<PageView> listActity(@RequestParam(value = "pageIndex",defaultValue = "1")int pageIndex,
                                     @RequestParam(value = "pageSize",defaultValue = "10")int pageSize) {
        PageView pageView = lotteryActivityService.queryActivityListByPage(pageIndex, pageSize);
        return CjResult.success(pageView);
    }

    @ApiOperation("获取活动详情")
    @PostMapping("list-activity")
    public CjResult<LotteryActivityInfoVo> listActity(@RequestParam(value = "pageIndex",defaultValue = "1")int pageIndex,
                                                      @RequestParam(value = "pageSize",defaultValue = "10")int pageSize) {
        PageView pageView = lotteryActivityService.queryActivityListByPage(pageIndex, pageSize);
        return CjResult.success(pageView);
    }

}
