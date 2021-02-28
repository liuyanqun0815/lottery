package com.cj.lottery.controller;

import com.alibaba.fastjson.JSON;
import com.cj.lottery.dao.CjCustomerLoginDao;
import com.cj.lottery.domain.CjCustomerAddress;
import com.cj.lottery.domain.CjCustomerLogin;
import com.cj.lottery.domain.CjPayNiuniuRecord;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.ConstumerAddressInfoVo;
import com.cj.lottery.domain.view.PayNiuniuRecordVo;
import com.cj.lottery.domain.view.UserInfoVo;
import com.cj.lottery.service.PayNiuniuRecordService;
import com.cj.lottery.service.UserInfoService;
import com.cj.lottery.util.ContextUtils;
import com.cj.lottery.util.HttpClientResult;
import com.cj.lottery.util.HttpClientUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private PayNiuniuRecordService payNiuniuRecordService;

    @Autowired
    private UserInfoService userInfoService;


    @ApiOperation("充值扭扭币记录列表")
    @PostMapping("list-pay-niuniu")
    public CjResult<List<PayNiuniuRecordVo>> listPayNiuniu() {
        int userId = ContextUtils.getUserId();
        List<PayNiuniuRecordVo> list = new ArrayList<>();
        List<CjPayNiuniuRecord> cjPayNiuniuRecords = payNiuniuRecordService.queryPayNiuniuRecordByConsumerId(userId);
        if (CollectionUtils.isEmpty(cjPayNiuniuRecords)){
            return CjResult.success(list);
        }
        return CjResult.success(cjPayNiuniuRecords.stream().map(s->PayNiuniuRecordVo.DoToVo(s)).collect(Collectors.toList()));
    }

    @ApiOperation("地址列表")
    @PostMapping("list-address")
    public CjResult<List<ConstumerAddressInfoVo>> listAddress() {
        int userId = ContextUtils.getUserId();
        return CjResult.success(userInfoService.queryAddressListByConstmerId(userId));
    }

    @ApiOperation("地址修改")
    @GetMapping("/update-address")
    public CjResult<String> updateAddress(@RequestBody ConstumerAddressInfoVo constumerAddressInfoVo){
        int result = userInfoService.updateUserAddress(constumerAddressInfoVo);
        if(result>0){
            return CjResult.success("保存成功");
        }
        return CjResult.fail("保存失败");
    }

    @ApiOperation("地址删除")
    @DeleteMapping("/delete-address")
    public CjResult<String> deleteAddress(@RequestParam("addressId")Integer addressId){
        int result = userInfoService.deleteUserAddress(addressId);
        if(result>0){
            return CjResult.success("删除成功");
        }
        return CjResult.fail("删除失败");
    }

    @ApiOperation("用户信息")
    @PostMapping("user-info")
    public CjResult<UserInfoVo> userInfo() {
        int userId = ContextUtils.getUserId();
        return CjResult.success(UserInfoVo.doToVo(userInfoService.queryUserInfoByCustomerId(userId)));
    }

}
