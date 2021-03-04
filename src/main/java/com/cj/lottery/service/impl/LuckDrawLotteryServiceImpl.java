package com.cj.lottery.service.impl;

import com.cj.lottery.controller.LuckDrawLotteryController;
import com.cj.lottery.dao.CjLotteryActivityDao;
import com.cj.lottery.dao.CjLotteryRecordDao;
import com.cj.lottery.dao.CjOrderPayDao;
import com.cj.lottery.dao.CjPrizePoolDao;
import com.cj.lottery.domain.CjLotteryActivity;
import com.cj.lottery.domain.CjOrderPay;
import com.cj.lottery.domain.CjPrizePool;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.LotteryActivityInfoVo;
import com.cj.lottery.domain.view.LotteryData;
import com.cj.lottery.enums.ActivityFlagEnum;
import com.cj.lottery.enums.ErrorEnum;
import com.cj.lottery.enums.PayStatusEnum;
import com.cj.lottery.service.LotteryActivityService;
import com.cj.lottery.service.LuckDrawLotteryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @Description: 抽奖service
 * @author: zhao_yd
 * @Date: 2021/2/28 4:24 下午
 */

@Slf4j
@Service
public class LuckDrawLotteryServiceImpl implements LuckDrawLotteryService {

    @Autowired
    private CjOrderPayDao cjOrderPayDao;
    @Autowired
    private CjLotteryActivityDao cjLotteryActivityDao;
    @Autowired
    private CjLotteryRecordDao cjLotteryRecordDao;
    @Autowired
    private CjPrizePoolDao prizePoolDao;
    @Autowired
    private LotteryActivityService lotteryActivityService;

    @Override
    @Transactional
    public CjResult<LotteryData> checkAuthority(Integer userId, String activityCode) {

        CjLotteryActivity activity = cjLotteryActivityDao.selectActivityByCode(activityCode);
        if (null == activity) {
            return CjResult.fail(ErrorEnum.NOT_ACITVITY);
        }
        //查询该用户有没有充值
        List<CjOrderPay> orderPayList = cjOrderPayDao.selectPaySuccessByUserId(userId);
        if (CollectionUtils.isEmpty(orderPayList)){
            return CjResult.fail(ErrorEnum.USER_NOT_PAY);
        }
        orderPayList = orderPayList.stream().filter(s -> s.getTotalFee().equals(activity.getConsumerMoney())).collect(Collectors.toList());
        if (orderPayList == null){
            return CjResult.fail(ErrorEnum.USER_PAY_NOT_ACITVITY);
        }
        //如果活动是新人活动，判断是否是新人
        if (activity.getActivityFlag() == ActivityFlagEnum.NEW_PEPOLE.getCode()) {
            List<CjOrderPay> orderPays = cjOrderPayDao.selectByUserId(userId);
            if (!CollectionUtils.isEmpty(orderPays)) {
                orderPays = orderPays.stream().filter(s -> s.getStatus() != PayStatusEnum.NO_PAY.getCode()).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(orderPays) && orderPays.size() > 1) {
                    return CjResult.fail(ErrorEnum.NOT_NEW_PEPOLE);
                }
            }
        }
        CjPrizePool pool = this.randomPrize(activity.getId());
        //去库存，根据 id+version进行更新
        int i = prizePoolDao.subtractionProductNum(pool.getId(),pool.getVersion());
        if (i<0){
            //防止高并发，去库存失败重新抽取---兜底方案
            while (true){
                pool = this.randomPrize(activity.getId());
                i = prizePoolDao.subtractionProductNum(pool.getId(),pool.getVersion());
                if (i>0){
                    break;
                }
            }
        }

        LotteryData data = new LotteryData();
        data.setOutTradeNo("abcdefghigklmn");
        data.setCallbackRate(70);
        data.setProductImgUrl("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa2.att.hudong.com%2F86%2F10%2F01300000184180121920108394217.jpg&refer=http%3A%2F%2Fa2.att.hudong.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1617438456&t=f5044b9b5f155d873bfa47abb52ac1e6");
        return CjResult.success(data);
//        if(null != activity){
//            //如果活动不是新用户活动都可以参加
//            if(!"1".equals(activity)){
//                return true;
//            }
//           return newOrNot(userId);
//        }
//        return false;
    }

    @Override
    public LotteryActivityInfoVo newPeopleActivities(Integer userId) {

        LotteryActivityInfoVo lotteryActivityInfoVo = new LotteryActivityInfoVo();
        List<CjOrderPay> orderPays = cjOrderPayDao.selectByUserId(userId);
        if (!CollectionUtils.isEmpty(orderPays)) {
            orderPays = orderPays.stream().filter(s -> s.getStatus() != PayStatusEnum.NO_PAY.getCode()).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderPays) && orderPays.size() > 1) {
                lotteryActivityInfoVo.setNewPeople(false);
                return lotteryActivityInfoVo;
            }
        }

        List<CjLotteryActivity> newPeopleActivities = cjLotteryActivityDao.getNewPeopleActivities();
        if(CollectionUtils.isEmpty(newPeopleActivities)){
            lotteryActivityInfoVo.setNewPeople(true);
            return lotteryActivityInfoVo;
        }

        //获取新人活动详情
        CjLotteryActivity activity = newPeopleActivities.get(0);
        lotteryActivityInfoVo = lotteryActivityService.queryActivityDetailsByPage(activity.getActivityCode());
        lotteryActivityInfoVo.setNewPeople(true);
        return lotteryActivityInfoVo;
    }

    public CjPrizePool randomPrize(int activityId) {
        List<CjPrizePool> cjPrizePools = prizePoolDao.selectProductByActivityId(activityId);
        CjPrizePool pool = new CjPrizePool();
        if (CollectionUtils.isEmpty(cjPrizePools)) {
            //没有可以抽取的商品，兜底方案，固定返回一个商品

        } else {
            pool = cjPrizePools.get(this.randomData(cjPrizePools.size()));
        }
        return pool;
    }

    private int randomData(int size){
        Random random = new Random();
        return random.nextInt(size);
    }

    /**
     * 订单金额通过一定的算法
     * 生成欧气值
     * @param totalFee
     * @return
     */
    public int getScore(int totalFee){
        Random random = new Random();
        int num = random.nextInt(9)+5;
        int score = (totalFee/10)*(num/10);
        score = totalFee * num /100;
        return score;
    }

    public static void main(String[] args) {
        LuckDrawLotteryServiceImpl controller = new LuckDrawLotteryServiceImpl();
        for (int i = 0; i < 35; i++) {
            System.out.println(controller.getScore(100));
//            controller.getScore(100);
        }
    }
}
