package com.cj.lottery.controller;


import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.service.OrderPayService;
import com.cj.lottery.util.ContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 支付controller
 *
 * @author lyq
 */
@RestController
@Slf4j
@RequestMapping("api/pay")
public class PayController {

@Autowired
private OrderPayService orderPayService;


    @PostMapping("wxgzhPay")
    public CjResult wxgzhPay(@RequestParam int totalFee,
                             @RequestParam int payType){
        int userId = ContextUtils.getUserId();

return null;

    }

}
