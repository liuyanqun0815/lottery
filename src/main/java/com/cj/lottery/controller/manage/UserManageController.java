package com.cj.lottery.controller.manage;

import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.PageView;
import com.cj.lottery.service.UserInfoService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/manage/user")
@Api(value = "后台用户信息", description = "用户信息管理")
@Slf4j
public class UserManageController {


    @Autowired
    private UserInfoService userInfoService;


    @GetMapping("list-user-base")
    public CjResult<PageView> listUserBase(@RequestParam(required = false, defaultValue = "1") int currentPage,
                                           @RequestParam(required = false, defaultValue = "10") int pageSize,
                                           @RequestParam(required = false) String account,
                                           @RequestParam(required = false) String customerCode,
                                           @RequestParam(required = false) String startTime,
                                           @RequestParam(required = false) String endTime,
                                           @RequestParam(required = false) String channel,
                                           @RequestParam(required = false) List<String> dateTime) {


        return userInfoService.listUserBaseInfo(currentPage, pageSize, account, customerCode, startTime, endTime, channel);
    }

    @GetMapping("list-pay-record")
    public CjResult<PageView> listUserPayRecord(@RequestParam(required = false, defaultValue = "1") int currentPage,
                                                @RequestParam(required = false, defaultValue = "10") int pageSize,
                                                @RequestParam(required = false) String account,
                                                @RequestParam(required = false) String customerCode,
                                                @RequestParam(required = false) String startTime,
                                                @RequestParam(required = false) String endTime,
                                                @RequestParam(required = false) Integer status,
                                                @RequestParam(required = false) String channel) {


        return userInfoService.listUserPayRecord(currentPage, pageSize, account, customerCode, startTime, endTime, status, channel);


    }

    @GetMapping("list-lottery-record")
    public CjResult<PageView> listUserLotteryRecord (
            @RequestParam(required = false, defaultValue = "1") int currentPage,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false) String account,
            @RequestParam(required = false) String customerCode,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String channel){


        return userInfoService.listUserLotteryRecord(currentPage, pageSize, account, customerCode, startTime, endTime, status, channel);
    }
    @GetMapping("list-channe")
    public CjResult<PageView> listChannel (
            @RequestParam(required = false, defaultValue = "1") int currentPage,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false) String channel,
            @RequestParam(required = false) String channelName){


        return userInfoService.listChannelRecord(currentPage, pageSize,  channel,channelName);
    }

    @GetMapping("login")
    public CjResult<Boolean> login (
            @RequestParam(required = false) String account,
            @RequestParam(required = false) String password){

        return userInfoService.login(account,password);
    }

}