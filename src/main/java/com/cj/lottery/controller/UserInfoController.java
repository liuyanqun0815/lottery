package com.cj.lottery.controller;

import com.cj.lottery.dao.CjCustomerLoginDao;
import com.cj.lottery.dao.CjNotifyPayDao;
import com.cj.lottery.dao.CjOrderPayDao;
import com.cj.lottery.domain.CjNotifyPay;
import com.cj.lottery.domain.CjOrderPay;
import com.cj.lottery.domain.CjPayScoreRecord;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.ConstumerAddressInfoVo;
import com.cj.lottery.domain.view.PayMoneyRecordVo;
import com.cj.lottery.domain.view.UserInfoVo;
import com.cj.lottery.service.PayNiuniuRecordService;
import com.cj.lottery.service.UserInfoService;
import com.cj.lottery.util.ContextUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private CjOrderPayDao orderPayDao;

    @Autowired
    private CjNotifyPayDao notifyPayDao;


    @ApiOperation("充值人民币列表")
    @PostMapping("list-pay-niuniu")
    public CjResult<List<PayMoneyRecordVo>> listPayNiuniu() {
        int userId = ContextUtils.getUserId();
        List<PayMoneyRecordVo> list = new ArrayList<>();
        List<CjNotifyPay> orderPays = notifyPayDao.selectSuccessByUserId(userId);
        if (CollectionUtils.isEmpty(orderPays)){
            return CjResult.success(list);
        }
        return CjResult.success(orderPays.stream().map(s->PayMoneyRecordVo.DoToVo(s)).collect(Collectors.toList()));
    }

    @ApiOperation("地址列表")
    @PostMapping("list-address")
    public CjResult<List<ConstumerAddressInfoVo>> listAddress() {
        int userId = ContextUtils.getUserId();
        return CjResult.success(userInfoService.queryAddressListByConstmerId(userId));
    }

    @ApiOperation("地址新增修改")
    @PostMapping("/saveOrupdate-address")
    public CjResult<String> updateAddress(@RequestParam(name = "id",required = false,defaultValue = "") Integer id,
                                          @RequestParam("name") String name,
                                          @RequestParam("phone") String phone,
                                          @RequestParam("address") String address){

        ConstumerAddressInfoVo constumerAddressInfoVo = new ConstumerAddressInfoVo();
        constumerAddressInfoVo.setId(id);
        constumerAddressInfoVo.setName(name);
        constumerAddressInfoVo.setAddress(address);
        constumerAddressInfoVo.setPhone(phone);
        int result = userInfoService.saveOrupdateUserAddress(constumerAddressInfoVo);
        if(result>0){
            return CjResult.success("操作成功");
        }
        return CjResult.fail("操作失败");
    }

    @ApiOperation("地址删除")
    @PostMapping("/delete-address")
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
        log.info("userInfo userId :{}",userId);
        return CjResult.success(UserInfoVo.doToVo(userInfoService.queryUserInfoByCustomerId(userId)));
    }

    public static void main(String[] args) {

        System.out.println(System.currentTimeMillis());
        System.out.println(Math.max(1,1111));
    }
}
