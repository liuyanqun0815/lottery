package com.cj.lottery.controller;

import com.alibaba.fastjson.JSON;
import com.cj.lottery.dao.CjCustomerLoginDao;
import com.cj.lottery.domain.CjCustomerLogin;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.util.ContextUtils;
import com.cj.lottery.util.HttpClientResult;
import com.cj.lottery.util.HttpClientUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liuyanqun
 */
@RestController
@RequestMapping("api/user")
@Api(value = "用户信息",description = "用户信息管理")
@Slf4j
public class UserInfoController {

    @Autowired
    private CjCustomerLoginDao cjCustomerLoginDao;

    @GetMapping("add-user")
    public CjResult<String> userController(HttpServletRequest request) throws Exception {
        int userId = ContextUtils.getUserId();
        log.info("param:{},add userId:{}",JSON.toJSONString(request.getParameterMap()),userId);
        CjCustomerLogin cjCustomerLogin = cjCustomerLoginDao.selectById(1);
        log.info("data:{}",JSON.toJSONString(cjCustomerLogin));
        return CjResult.success(JSON.toJSONString(request.getParameterMap()));
    }

}
