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
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
@Component
public class SmsUtil {

    @Value("${msg.account}")
    private String account;
    @Value("${msg.password}")
    private String password;
    @Value("${msg.sendMsgUrl}")
    private String sendMsgUrl;

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
        String kaptcha = getKaptcha();
        Map<String, String> map = Maps.newHashMap();
        map.put("userid", "");
        map.put("account",account);//用户帐号，由系统管理员
        map.put("password",DigestUtils.md5DigestAsHex(password.getBytes()).toUpperCase());//用md5加密方式，md5采用32位大写
        map.put("mobile",mobile);//短信发送的目的号码.多个号码之间用半角逗号隔开
        map.put("sendTime","");  //为空表示立即发送，定时发送格式2019-10-24 09:08:10
        map.put("action","send"); //设置为固定的:send
        map.put("extno","");   //请先询问配置的通道是否支持扩展子号
        map.put("content","【客云科技】验证码："+kaptcha+"。您正在进行手机号码验证，如非本人操作，请忽略改短信");

        try {
            HttpClientResult httpClientResult = HttpClientUtils.doPost(sendMsgUrl, map);
            if (httpClientResult.getCode() != 200){
                log.info("send sms data:{}",JSONObject.toJSON(httpClientResult));
            }
            JSONObject data = JSONObject.parseObject(httpClientResult.getContent());
            String returnstatus = data.getString("returnstatus");
            if("Success".equals(returnstatus)){
                String taskId = data.getString("taskID");
                // 短信发送成功，将验证码存储到redis，并设置过期时间为5分钟
                smsCache.put(mobile, kaptcha);
                log.info("短信验证码生成 手机号:[{}] 验证码:[{}],taskId:[{}] ", mobile, kaptcha,taskId);
            }else {
                log.info("send sms fail:{}",JSONObject.toJSON(httpClientResult));
            }
            return null;
        } catch (Exception e) {
            log.info("sendSms fail:",e);
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

    public static void main(String[] args) {
        System.out.println(407940618&255);
    }


}
