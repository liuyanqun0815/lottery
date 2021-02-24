package com.cj.lottery.controller.common;

import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.enums.ErrorEnum;
import com.cj.lottery.util.UuidUtils;
import com.cj.lottery.util.ValidUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author liuyanqun
 */
@Slf4j
@RestController
@RequestMapping("api/cj/login")
public class LoginController {


    @ApiOperation("发送验证码")
    @PostMapping("send-sms-code")
    public CjResult<Void> sendSmsCode(@ApiParam("手机号") @RequestParam("mobile") String mobile) {
        return CjResult.success();
    }

    /**
     * 验证手机号是否正确
     *
     * @param mobile
     * @return
     */
    @ApiOperation("验证手机号是否正确")
    @PostMapping("verify-mobile")
    public CjResult<Boolean> verifyMobile(@RequestParam("mobile") String mobile) {
        return CjResult.success(ValidUtil.phoneCheck(mobile));
    }

    /**
     * 登录校验
     *
     * @param mobile
     * @param code
     * @return
     */
    @ApiOperation("登录校验")
    @PostMapping("get-token")
    public CjResult<String> getToken(@RequestParam("mobile") String mobile, @RequestParam("code") String code) {
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(code)) {
            return CjResult.fail(ErrorEnum.PARAM_ERROR);
        }

        return CjResult.success(UuidUtils.getUUid());
    }

}
