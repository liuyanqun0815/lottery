package com.cj.lottery.controller;


import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.service.OrderPayService;
import com.cj.lottery.util.ContextUtils;
//import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
//import com.github.binarywang.wxpay.service.WxPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 支付controller
 *
 * @author lyq
 */
@Api(value = "支付",description = "支付相关接口")
@RestController
@Slf4j
@RequestMapping("api/pay")
public class PayController {

    @Autowired
    private OrderPayService orderPayService;

//    @Autowired
//    private WxPayService wxService;

    @ApiOperation("微信公众号充值接口")
    @PostMapping("wxgzhPay")
    public CjResult wxgzhPay(@ApiParam("充值金额(元)") @RequestParam int totalFee) {
        int userId = ContextUtils.getUserId();
        return orderPayService.createWxOrderPay(userId, totalFee);

    }

    @ApiOperation("微信支付回调接口")
    @PostMapping("notify")
    public CjResult wxNotify(HttpServletRequest request) {

        try {
//            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
//            WxPayOrderNotifyResult notifyResult = wxService.parseOrderNotifyResult(xmlResult);
//            if ("SUCCESS".equals(notifyResult.getResultCode())){
//
//
//            }
        }catch (Exception ex){
            log.error("wxNotify exception:",ex);
        }

        return orderPayService.wxOrderNotify();

    }

}
