package com.cj.lottery.controller.simple;


import com.cj.lottery.domain.ImgNameVo;
import com.cj.lottery.domain.simple.CjSimpleLotteryActivity;
import com.cj.lottery.domain.simple.CjSimpleOrderPay;
import com.cj.lottery.domain.simple.CjSimpleProductInfo;
import com.cj.lottery.domain.simple.view.SimpleActivityVo;
import com.cj.lottery.domain.simple.view.SimpleProductVo;
import com.cj.lottery.domain.view.CjLotteryMaopaoVo;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.PaySuccessVo;
import com.cj.lottery.enums.BuriedPointEnum;
import com.cj.lottery.enums.ErrorEnum;
import com.cj.lottery.enums.PayStatusEnum;
import com.cj.lottery.enums.PayTypeEnum;
import com.cj.lottery.service.LuckDrawLotteryService;
import com.cj.lottery.service.OrderPayService;
import com.cj.lottery.service.SimpleActivityService;
import com.cj.lottery.util.IpUtil;
import com.google.common.cache.Cache;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Api(value = "活动",description = "简洁版活动接口")
@Slf4j
@RestController
@RequestMapping("api/cj/simple/activity")
public class SimpleActivityController {



    @Autowired
    private SimpleActivityService simpleActivityService;
    @Autowired
    public Cache<String, String> smsCache;
    @Autowired
    private OrderPayService orderPayService;
    @Autowired
    private LuckDrawLotteryService luckDrawLotteryService;

    @ApiOperation("获取活动列表")
    @PostMapping("list-activity")
    public CjResult<List<SimpleActivityVo>> listActity(HttpServletRequest request,
                                                 @ApiParam("渠道") @RequestParam(required = false)String channel,
                                                 @ApiParam("活动编码")@RequestParam(required = false) String activityCode) {
        List<SimpleActivityVo> voList = Lists.newArrayList();
        List<CjSimpleLotteryActivity> activities = simpleActivityService.queryActivityList();
        if (CollectionUtils.isEmpty(activities)){
            return CjResult.success(voList);
        }
        activities = activities.stream().sorted(Comparator.comparing(CjSimpleLotteryActivity::getSort)).collect(Collectors.toList());
        List<Integer> activityIdList = activities.stream().map(CjSimpleLotteryActivity::getId).collect(Collectors.toList());
        List<CjSimpleProductInfo> productInfo =  simpleActivityService.queryProductByActivityIds(activityIdList);
        Map<Integer, List<CjSimpleProductInfo>> activityMap = productInfo.stream().collect(Collectors.groupingBy(CjSimpleProductInfo::getActivityId));
        for(CjSimpleLotteryActivity activity : activities){
            List<CjSimpleProductInfo> productInfos = activityMap.get(activity.getId());
            if (CollectionUtils.isEmpty(productInfos)){
                continue;
            }
            SimpleActivityVo vo = new SimpleActivityVo();
            vo.setActivityCode(activity.getActivityCode());
            vo.setActivityName(activity.getActivityName());
            vo.setMoney(activity.getMoney());
            productInfos = productInfos.stream().filter(s->s.getLuck()==1).collect(Collectors.toList());
            CjSimpleProductInfo info = productInfos.get(randomData(productInfos.size()));
            vo.setProductCode(info.getProductCode());
            productInfos = productInfos.stream().sorted(Comparator.comparing(CjSimpleProductInfo::getSort)).collect(Collectors.toList());
            vo.setBodyUrlList(productInfos.stream().map(s->ImgNameVo.ImgVo(s)).collect(Collectors.toList()));
            voList.add(vo);
            if (!StringUtils.isEmpty(activityCode) && activity.getActivityCode().equals(activityCode)){
                return CjResult.success(Lists.newArrayList(vo));
            }
        }
        return CjResult.success(voList);
    }

    @ApiOperation("H5充值接口")
    @PostMapping("wxH5Pay")
    public CjResult<PaySuccessVo> wxH5Pay(HttpServletRequest request,
                                          HttpServletResponse response,
                                          @ApiParam("充值金额(分)") @RequestParam int totalFee,
                                          @ApiParam("活动编码")@RequestParam String activityCode,
                                          @ApiParam("产品编码")@RequestParam String productCode,
                                          @ApiParam("支付类型")@RequestParam(required = false,defaultValue = "WX_H5") PayTypeEnum payType,
                                          @ApiParam("渠道")@RequestParam(required = false)String channel) {
        String ipAddr = IpUtil.getIpAddr(request);
        if (ObjectUtils.isEmpty(ipAddr)){
            return CjResult.fail(ErrorEnum.IP_ERROR);
        }
        CjSimpleLotteryActivity activityVo = simpleActivityService.queryActivityByCode(activityCode);
        if (activityVo == null){
            return CjResult.fail("活动不存在");
        }
        List<CjSimpleProductInfo> productInfos = simpleActivityService.queryProductByActivityIds(Lists.newArrayList(activityVo.getId()));
        List<String> luckList = productInfos.stream().filter(s -> s.getLuck() == 1).map(CjSimpleProductInfo::getProductCode).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(luckList) || !luckList.contains(productCode)){
            return CjResult.fail("奖品抽取失败");
        }
        if (PayTypeEnum.WX_H5 == payType){
            return orderPayService.createSimpleWxH5OrderPay(ipAddr,activityVo,productCode,channel);
        }else if (PayTypeEnum.ALI_H5 == payType){
            return orderPayService.createSimpleAliH5OrderPay(ipAddr,activityVo,response,channel);
        }
        return CjResult.fail("支付类型有误");

    }

    @ApiOperation("查询订单是否支付成功")
    @PostMapping("query-order-status")
    public CjResult<Boolean> queryOrderStatus(HttpServletRequest request,
                                              @ApiParam("订单号")@RequestParam String outTradeNo,
                                              @ApiParam("随机字符串")@RequestParam String random) {
        String randomData = smsCache.getIfPresent(outTradeNo);
        if (!random.equals(randomData)){
            return CjResult.fail("订单已失效");
        }
        CjSimpleOrderPay orderPay =  simpleActivityService.queryOrderPay(outTradeNo);
        boolean flag = false;
        if (orderPay != null && PayStatusEnum.PAY.getCode() == orderPay.getStatus()){
            flag = true;
        }
        return CjResult.success(flag);
    }

    @ApiOperation("我要发货")
    @PostMapping("send-prize")
    public CjResult<Void> sendPrize(@ApiParam("产品编码") @RequestParam String productCode,
                                    @ApiParam("活动编码")@RequestParam String activityCode,
                                    @ApiParam("姓名")@RequestParam String name,
                                    @ApiParam("电话")@RequestParam String mobile,
                                    @ApiParam("所在区域")@RequestParam String address,
                                    @ApiParam("详细地址")@RequestParam String addressInfo,
                                    @ApiParam("渠道")@RequestParam(required = false)String channel,
                                    @ApiParam("订单号")@RequestParam String outTradeNo) {
        CjSimpleOrderPay orderPay =  simpleActivityService.queryOrderPay(outTradeNo);
        if (orderPay== null){
            return CjResult.fail("发货失败");
        }
        if (orderPay.getStatus() != PayStatusEnum.PAY.getCode()){
            return CjResult.fail("订单已发货");
        }
        CjSimpleLotteryActivity activityVo = simpleActivityService.queryActivityByCode(activityCode);
        if (activityVo == null){
            return CjResult.fail("活动不存在");
        }
        CjSimpleProductInfo productInfo = simpleActivityService.queryProductByCode(productCode);
        if (productInfo == null){
            return CjResult.fail("奖品不存在");
        }
        List<CjSimpleProductInfo> productInfos = simpleActivityService.queryProductByActivityIds(Lists.newArrayList(activityVo.getId()));
        List<String> luckList = productInfos.stream().filter(s -> s.getLuck() == 1).map(CjSimpleProductInfo::getProductCode).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(luckList) || !luckList.contains(productCode)){
            return CjResult.fail("奖品不一致");
        }
        simpleActivityService.sendPrize(name,mobile,address,addressInfo,channel,outTradeNo,productInfo.getId());
        return CjResult.success(null);
    }

    @ApiOperation("埋点接口")
    @PostMapping("buried-point")
    public CjResult<Void> buriedPoints(HttpServletRequest request,
                                       @ApiParam("埋点类型") @RequestParam BuriedPointEnum buriedPoint,
                                       @ApiParam("活动编码") @RequestParam String activityCode,
                                       @ApiParam("渠道")@RequestParam(required = false) String channel) {

        String ip = IpUtil.getIpAddr(request);
        if (ObjectUtils.isEmpty(ip)){
            return CjResult.fail(ErrorEnum.IP_ERROR);
        }
        String ua = request.getHeader("User-Agent");
        CjSimpleLotteryActivity activityVo = simpleActivityService.queryActivityByCode(activityCode);
        if (activityVo == null){
            return CjResult.fail("活动不存在");
        }
        simpleActivityService.buriedPoints(activityVo.getId(),buriedPoint.getCode(),ip,channel,ua);
        return CjResult.success(null);
    }

    @ApiOperation("获取弹幕列表")
    @PostMapping("list-danmu")
    public CjResult<List<CjLotteryMaopaoVo>> listDanmu(@RequestParam(value = "activityCode") String activityCode){
        //通过固定配置信息返回弹幕
        return luckDrawLotteryService.getSimpleAwardwinningUserInfo(activityCode);
    }

    private int randomData(int size) {
        Random random = new Random();
        return random.nextInt(size);
    }
}
