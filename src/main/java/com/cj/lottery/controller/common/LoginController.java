package com.cj.lottery.controller.common;

import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.enums.ErrorEnum;
import com.cj.lottery.service.UserInfoService;
import com.cj.lottery.util.SmsUtil;
import com.cj.lottery.util.UuidUtils;
import com.cj.lottery.util.ValidUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author liuyanqun
 */
@Api(value = "H5登录",description = "H5登录接口")
@Slf4j
@RestController
@RequestMapping("api/cj/login")
public class LoginController {



    @Autowired
    private SmsUtil smsUtil;
    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation("发送验证码")
    @PostMapping("send-sms-code")
    public CjResult<Void> sendSmsCode(@ApiParam("手机号") @RequestParam("mobile") String mobile) {
        String data = smsUtil.sendSms(mobile);
        if (!ObjectUtils.isEmpty(data)) {
            return CjResult.fail(data);
        }
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
    public CjResult<Boolean> verifyMobile(@RequestParam("mobile") String mobile,
                                          @RequestParam("code")String code) {
        Boolean aBoolean = smsUtil.checkKaptcha(mobile, code);
        return CjResult.success(aBoolean);
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
        boolean phone = ValidUtil.phoneCheck(mobile);
        if (!phone){
            return CjResult.fail(ErrorEnum.PHONE_FORMAT_ERROR);
        }
        Boolean aBoolean = smsUtil.checkKaptcha(mobile, code);
        if (aBoolean){
            String token = userInfoService.queryLatestToken(mobile);
            return CjResult.success(token);
        }
        return CjResult.fail(ErrorEnum.SMS_CODE);
    }

    @PostMapping("test-mobile")
    public CjResult<String> testCachePut(@RequestParam("mobile") String mobile) {
        String code = smsUtil.put(mobile);
        return CjResult.success(code);
    }

}
