package com.cj.lottery.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.cj.lottery.enums.ErrorEnum;
import com.google.common.cache.Cache;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@Component
public class SmsUtil {


    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.secret}")
    private String secret;

    @Value("${aliyun.signName}")
    private String signName;

    @Value("${aliyun.templateCode}")
    private String templateCode;

    @Value("${aliyun.paramName}")
    private String paramName;

    @Autowired
    public Cache<String, String> smsCache;


    /**
     * 发送短信.
     *
     * @param mobile 手机号
     * @return String
     */
    @SuppressWarnings("deprecated")
    public String sendSms(String mobile) {
        // 检验手机号码
        boolean isPhone = ValidUtil.phoneCheck(mobile);
        if (!isPhone) {
            return ErrorEnum.PHONE_FORMAT_ERROR.getDesc();
        }
        // 校验是否重复发送
        String smsCode = smsCache.getIfPresent(mobile);
        if (smsCode != null) {
            return ErrorEnum.SMS_FREQUENTLY.getDesc();
        }
        String kaptcha = this.getKaptcha();
        Map<String, String> map = new HashMap<>(1);
        map.put(this.paramName, kaptcha);
        String templateParam = JSON.toJSONString(map);

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou",
                this.accessKeyId, this.secret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", this.signName);
        request.putQueryParameter("TemplateCode", this.templateCode);
        request.putQueryParameter("TemplateParam", templateParam);

        try {
            CommonResponse response = client.getCommonResponse(request);
            String responseMessage = JSONObject.parseObject(response.getData())
                    .getString("Message");
            if (!"ok".equals(responseMessage)) {
                // 短信发送成功，将验证码存储到redis，并设置过期时间为5分钟
                smsCache.put(mobile, kaptcha);
                log.info("短信验证码生成 手机号:[{}] 验证码:[{}] ", mobile, kaptcha);
                return null;
            }
        } catch (Exception e) {
            // 短信发送失败
            return ErrorEnum.SMS_FAILED.getDesc();
        }
        // 短信发送失败
        return ErrorEnum.SMS_FAILED.getDesc();

    }

    /**
     * 校验短信验证码.
     *
     * @param mobile  mobile
     * @param kaptcha kaptcha
     * @return 校验结果
     */
    public Boolean checkKaptcha(String mobile, String kaptcha) {
        String s = this.smsCache.getIfPresent(mobile);
        return s != null && s.equals(kaptcha);
    }

    public String put(String mobile) {
        String kaptcha = this.getKaptcha();
        smsCache.put(mobile, kaptcha);
        return kaptcha;
    }

    /**
     * 生成短信验证码并存储.
     *
     * @return kaptcha
     */
    private String getKaptcha() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 4; i++) {
            // 生成一个[0,10)
            int randomInt = random.nextInt(10);
            str.append(randomInt);
        }
        return str.toString();
    }


}
