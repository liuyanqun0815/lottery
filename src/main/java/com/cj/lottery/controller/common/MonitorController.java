package com.cj.lottery.controller.common;

import com.cj.lottery.constant.ContextCons;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.enums.MaidianTypeEnum;
import com.cj.lottery.service.CustomerLoginService;
import com.cj.lottery.service.MaidianService;
import com.cj.lottery.util.IpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(value = "监控",description = "")
@Slf4j
@RestController
@RequestMapping("api/cj/monitor")
public class MonitorController {

    @Autowired
    private MaidianService maidianService;
    @Autowired
    private CustomerLoginService customerLoginService;


    @ApiOperation("埋点接口")
    @PostMapping("buribed-point")
    public CjResult<Void> maidian(HttpServletRequest request,
                                  @RequestParam MaidianTypeEnum typeEnum,
                                  @RequestParam(required = false)String activityCode,
                                  @RequestParam(required = false)String channel){


        String token = request.getHeader(ContextCons.TOKEN);
        Integer userId = null;
        if (com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isNotEmpty(token)) {
            userId = customerLoginService.getUserIdByToken(token);
        }
        String ipAddr = IpUtil.getIpAddr(request);
        int i = maidianService.saveRecord(typeEnum, userId, ipAddr, activityCode, channel);
        return CjResult.success();
    }

}
