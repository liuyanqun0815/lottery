package com.cj.lottery.util;

import com.alibaba.fastjson.JSONObject;
import com.cj.lottery.enums.ErrorEnum;
import com.google.common.cache.Cache;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

@Slf4j
@Component
public class SmsUtil {

//    @Value("${msg.account}")
//    private String account;
//    @Value("${msg.password}")
//    private String password;
//    @Value("${msg.sendMsgUrl}")
//    private String sendMsgUrl;
//    private String useTimeUrl = "http:// 211.149.255.237:81/smsJson.aspx";

    @Autowired
    public Cache<String, String> smsCache;


    private String spid = "613312";

    private String password = "keyun613312";

    private String ac = "1069613312";

    private String sendMsgUrl = "https://smsjm.jxtebie.com/sms/submit";
    /**
     * 发送短信.
     *
     * @param mobile 手机号
     * @return String
     */
    @SuppressWarnings("deprecated")
    public String sendSms(String mobile,String ipAddress) {
        // 检验手机号码
        boolean isPhone = ValidUtil.phoneCheck(mobile);
        if (!isPhone) {
            return ErrorEnum.PHONE_FORMAT_ERROR.getDesc();
        }
        // 校验是否重复发送
        String smsCode = smsCache.getIfPresent(mobile);
        String num = smsCache.getIfPresent(ipAddress);
        log.info("sendSms param mobile:{}  ipAddress:{},num:{}",mobile,ipAddress,num);
        if (smsCode != null || (num != null && Integer.parseInt(num) > 3)) {
            return ErrorEnum.SMS_FREQUENTLY.getDesc();
        }
        String kaptcha = getKaptcha();
        Map<String, String> map = Maps.newHashMap();
//        map.put("userid", "");
//        map.put("account",account);//用户帐号，由系统管理员
//        map.put("password",DigestUtils.md5DigestAsHex(password.getBytes()).toUpperCase());//用md5加密方式，md5采用32位大写
//        map.put("mobile",mobile);//短信发送的目的号码.多个号码之间用半角逗号隔开
//        map.put("sendTime","");  //为空表示立即发送，定时发送格式2019-10-24 09:08:10
//        map.put("action","send"); //设置为固定的:send
//        map.put("extno","");   //请先询问配置的通道是否支持扩展子号
//        map.put("content","【客云科技】验证码："+kaptcha+"。您正在进行手机号码验证，如非本人操作，请忽略改短信");

        map.put("spid",spid);
        map.put("password",password);
        map.put("ac",ac);
        map.put("mobiles",mobile);
        map.put("content","【客云科技】验证码："+kaptcha+"。您正在进行手机号码验证，如非本人操作，请忽略改短信");

        try {
            HttpClientResult httpClientResult = HttpClientUtils.doPost(sendMsgUrl, map);
            if (httpClientResult.getCode() != 200){
                log.info("send sms data:{}",JSONObject.toJSON(httpClientResult));
            }
            String content = httpClientResult.getContent();
            boolean flag = parseXml(content);
            if(flag){
                // 短信发送成功，将验证码存储到redis，并设置过期时间为5分钟
                smsCache.put(mobile, kaptcha);
                int times = 0;
                if (ObjectUtils.isEmpty(num)){
                    times = 1;
                }else {
                    times = Integer.parseInt(num)+1;
                }
                smsCache.put(ipAddress,times+"");
                log.info("短信验证码生成 手机号:[{}] 验证码:[{}] ", mobile, kaptcha);
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

    public boolean parseXml(String xml) {
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element root = doc.getRootElement();// 指向根节点
        Iterator it = root.elementIterator();
        boolean flag = false;
        while (it.hasNext()) {
            Element element = (Element) it.next();// 一个Item节点
            String name = element.getName();
            System.out.println(element.getName() + " : " + element.getTextTrim());
            if ("result".equals(name) && Integer.parseInt(element.getTextTrim())== 0){
                flag = true;
                break;
            }
        }
        return flag;
    }


    public int msgUserTime() {
        Map<String, String> map = Maps.newHashMap();
        map.put("spid", spid);
        map.put("password",password);//用md5加密方式，md5采用32位大写
        map.put("action","querybalance");
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult = HttpClientUtils.doPost("http://baljm.jxtebie.com/xunjintec/intfgwsms/spbalance.html", map);
        } catch (Exception e) {
            log.info("msgUserTime exception:",e);
        }
        if (httpClientResult.getCode() != 200){
            log.info("send sms data:{}",JSONObject.toJSON(httpClientResult));
        }
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(httpClientResult.getContent());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element root = doc.getRootElement();// 指向根节点
        Iterator it = root.elementIterator();
        int count = 0;
        while (it.hasNext()) {
            Element element = (Element) it.next();// 一个Item节点
            String name = element.getName();
            System.out.println(element.getName() + " : " + element.getTextTrim());
            if ("desc".equals(name)){
                count = Integer.parseInt(element.getTextTrim());
                break;
            }
        }
        return count;
    }

    public Object pushStatus() {

        Map<String, String> map = Maps.newHashMap();
        map.put("spid", spid);
        map.put("password",password);//用md5加密方式，md5采用32位大写
        HttpClientResult httpClientResult = null;
        try {
            httpClientResult = HttpClientUtils.doPost("https://rptjm.jxtebie.com/sms/report", map);
        } catch (Exception e) {
            log.info("msgUserTime exception:",e);
        }
        if (httpClientResult.getCode() != 200){
            log.info("send sms data:{}",JSONObject.toJSON(httpClientResult));
        }
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(httpClientResult.getContent());
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        System.out.println(httpClientResult.getContent());
        Element root = doc.getRootElement();// 指向根节点
        Iterator it = root.elementIterator();
        int count = 0;
        while (it.hasNext()) {
            Element element = (Element) it.next();// 一个Item节点
            String name = element.getName();
            System.out.println(element.getName() + " : " + element.getTextTrim());
        }
        return null;

    }
}
