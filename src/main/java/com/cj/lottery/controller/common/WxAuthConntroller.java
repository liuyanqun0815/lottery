package com.cj.lottery.controller.common;

import com.alibaba.fastjson.JSONObject;
import com.cj.lottery.dao.CjCustomerInfoDao;
import com.cj.lottery.dao.CjCustomerLoginDao;
import com.cj.lottery.dao.CjCustomerLoginLogDao;
import com.cj.lottery.domain.CjCustomerInfo;
import com.cj.lottery.domain.CjCustomerLogin;
import com.cj.lottery.domain.CjCustomerLoginLog;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.service.UserInfoService;
import com.cj.lottery.util.AuthUtil;
import com.cj.lottery.util.UuidUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/wxapi")
public class WxAuthConntroller {

    @Value("${app.weixin.gzh.appid}")
    String appidgzh;

    @Value("${app.weixin.gzh.secret}")
    String secretgzh;

    @Value("${app.domain.url}")
    String domain;

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private CjCustomerLoginDao cjCustomerLoginDao;
    @Autowired
    private CjCustomerInfoDao customerInfoDao;
    @Autowired
    private CjCustomerLoginLogDao cjCustomerLoginLogDao;

    @PostMapping("/weixin/login")
    @ApiOperation(value = "微信授权登录", httpMethod = "POST", produces = "application/json;charset=UTF-8")
    public void weixinLogin(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false,defaultValue = "lottery_channel") String channel) {

        try {
            //这里是回调的url
            String redirect_uri = URLEncoder.encode(domain + "/wxapi/weixin/callBack", "UTF-8");
            String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
                    "appid=APPID" +
                    "&redirect_uri=REDIRECT_URI" +
                    "&response_type=code" +
                    "&scope=SCOPE" +
                    "&state=123#wechat_redirect";
            url.replace("APPID", appidgzh).replace("REDIRECT_URI", redirect_uri).replace("SCOPE", "snsapi_userinfo");
            log.info("weixinLogin forward重定向地址：{}", url);
            response.sendRedirect(url);
        } catch (Exception e) {
            log.error("weixinLogin exception:", e);
        }

    }

    @GetMapping("/weixin/callBack")
    @ApiOperation(value = "微信授权回调", httpMethod = "GET", produces = "application/json;charset=UTF-8")
    public CjResult<String> weixinCallBack(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> map = new HashMap<>();
            //获取回调地址中的code
            String code = request.getParameter("code");
            //拼接url
            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appidgzh + "&secret="
                    + secretgzh + "&code=" + code + "&grant_type=authorization_code";
            JSONObject jsonObject = AuthUtil.doGetJson(url);
            if (jsonObject.containsKey("errcode")) {
                Integer errcode = jsonObject.getInteger("errcode");
                if (null != errcode) {
                    String errmsg = jsonObject.getString("errmsg");
                    return CjResult.fail(errmsg);
                }
            }
            //1.获取微信用户的openid
            String openid = jsonObject.getString("openid");
            //2.获取获取access_token
            String access_token = jsonObject.getString("access_token");
            String infoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid
                    + "&lang=zh_CN";
            //3.获取微信用户信息
            JSONObject userInfo = AuthUtil.doGetJson(infoUrl);
            //去数据库查询此微信是否注册过
            CjCustomerLogin loginInfo = userInfoService.queryLoginInfoByLoginPhone(openid);
            if (loginInfo != null) {
                String data = userInfoService.queryLatestToken(openid);
                return CjResult.success(data);
            }
            String nickname = userInfo.getString("nickname");
            Integer sex = userInfo.getInteger("sex");
            String headimgurl = userInfo.getString("headimgurl");
            String token = this.saveUserInfo(openid, nickname, sex, headimgurl);
            return CjResult.success(token);
        } catch (Exception e) {
            log.error("微信回调失败:", e);
            return CjResult.fail("微信授权回调失败！");
        }
    }

    /**
     * 首次登录，保存用户信息
     *
     * @param loginMark
     * @param nickname
     * @param sex
     * @param headimgurl
     * @return
     */
    private String saveUserInfo(String loginMark, String nickname, Integer sex, String headimgurl) {
        CjCustomerLogin login = new CjCustomerLogin();
        login.setLoginPhone(loginMark);
        cjCustomerLoginDao.insert(login);
        CjCustomerInfo info = new CjCustomerInfo();
        info.setCustomerName(nickname);
        info.setSex(sex);
        info.setHeadUrl(headimgurl);
        info.setCustomerId(login.getId());
        customerInfoDao.insertSelective(info);
        String uniqueCode = UuidUtils.getUUid();
        CjCustomerLoginLog loginLog = new CjCustomerLoginLog();
        loginLog.setCustomerId(login.getId());
        loginLog.setUniqueCode(uniqueCode);
        cjCustomerLoginLogDao.insertSelective(loginLog);
        return uniqueCode;
    }



    @GetMapping("/register")
    public String register(String openid, ModelMap map) {
        map.put("openid", openid);
        return "/upload";  // 我这里是打开上传页面，可根据自己业务需要实际来跳转
    }

    @GetMapping("/success")
    public String register() {
        return "/success";  // 打开注册成功页面
    }
}
