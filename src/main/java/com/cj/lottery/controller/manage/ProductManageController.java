package com.cj.lottery.controller.manage;


import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.EnumVo;
import com.cj.lottery.domain.view.PageView;
import com.cj.lottery.service.LotteryActivityService;
import com.cj.lottery.service.ProductInfoService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/manage/product")
@Api(value = "后台产品信息", description = "后台产品信息")
@Slf4j
public class ProductManageController {

    @Autowired
    private ProductInfoService productInfoService;
    @Autowired
    private LotteryActivityService lotteryActivityService;

    @GetMapping("list-product")
    public CjResult<PageView> listProduct(@RequestParam(required = false, defaultValue = "1") int currentPage,
                                          @RequestParam(required = false, defaultValue = "10") int pageSize,
                                          @RequestParam(required = false) String productName) {


        return productInfoService.pageProduct(currentPage, pageSize, productName);
    }

    @GetMapping("list-product-pool")
    public CjResult<PageView> listProductPool(@RequestParam(required = false, defaultValue = "1") int currentPage,
                                              @RequestParam(required = false, defaultValue = "10") int pageSize,
                                              @RequestParam(required = false) String productName,
                                              @RequestParam(required = false) String activityCode,
                                              @RequestParam(required = false) Integer status,
                                              @RequestParam(required = false) Integer used) {


        return productInfoService.pageProductPool(currentPage, pageSize, productName,activityCode,status,used);
    }

    @GetMapping("all-activity")
    public CjResult<List<EnumVo>> allActivity() {
        return lotteryActivityService.queryAllActivity();
    }
}
