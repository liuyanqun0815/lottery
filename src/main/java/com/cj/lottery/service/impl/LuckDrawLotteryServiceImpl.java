package com.cj.lottery.service.impl;

import com.cj.lottery.controller.LuckDrawLotteryController;
import com.cj.lottery.dao.*;
import com.cj.lottery.domain.*;
import com.cj.lottery.domain.view.*;
import com.cj.lottery.enums.*;
import com.cj.lottery.event.EventPublishService;
import com.cj.lottery.mapper.CjProductInfoMapper;
import com.cj.lottery.service.LotteryActivityService;
import com.cj.lottery.service.LuckDrawLotteryService;
import com.cj.lottery.service.UserInfoService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
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
    private CjPayScoreRecordDao payScoreRecordDao;
    @Autowired
    private CjProductInfoDao productInfoDao;
    @Autowired
    private LotteryActivityService lotteryActivityService;
    @Autowired
    private EventPublishService eventPublishService;
    @Autowired
    private CjLotteryRecordDao lotteryRecordDao;
    @Autowired
    private UserInfoService userInfoService;

    @Override
    @Transactional
    public CjResult<LotteryData> clickLottery(Integer userId, String activityCode, boolean test) {

        CjLotteryActivity activity = cjLotteryActivityDao.selectActivityByCode(activityCode);
        if (null == activity) {
            return CjResult.fail(ErrorEnum.NOT_ACITVITY);
        }
        int score = getScore(activity.getConsumerMoney());
        //试玩--@todo 待优化
        if (test) {
            return CjResult.success(this.testLottery(score, activity));
        }
        //查询该用户有没有充值
        List<CjOrderPay> orderPayList = cjOrderPayDao.selectPaySuccessByUserId(userId);
        if (CollectionUtils.isEmpty(orderPayList)) {
            return CjResult.fail(ErrorEnum.USER_NOT_PAY);
        }
        CjOrderPay orderPay = orderPayList.stream().filter(s -> s.getTotalFee().equals(activity.getConsumerMoney())).findFirst().get();
        if (orderPay == null) {
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
        int i = prizePoolDao.subtractionProductNum(pool.getId(), pool.getVersion());
        if (i < 0) {
            //防止高并发，去库存失败重新抽取---兜底方案
            while (true) {
                pool = this.randomPrize(activity.getId());
                i = prizePoolDao.subtractionProductNum(pool.getId(), pool.getVersion());
                if (i > 0) {
                    break;
                }
            }
        }
        //更新订单状态为已核销
        this.cjOrderPayDao.updateStatusById(orderPay.getId(), PayStatusEnum.USED.getCode());

        //记录抽奖记录
        CjLotteryRecord record = new CjLotteryRecord();
        record.setOrderId(orderPay.getId());
        record.setProductId(pool.getProductId());
        record.setCustomerId(userId);
        record.setStatus(PrizeStatusEnum.dai_fa_huo.getCode());
        cjLotteryRecordDao.insertSelective(record);

        //记录欧气值生成记录
        CjPayScoreRecord scoreRecord = new CjPayScoreRecord();
        scoreRecord.setCustomerId(userId);
        scoreRecord.setScore(score);
        scoreRecord.setOrderId(orderPay.getId());
        this.payScoreRecordDao.insertSelective(scoreRecord);

        LotteryData data = new LotteryData();
        data.setOutTradeNo(orderPay.getOutTradeNo());
        data.setCallbackRate(activity.getActivityRate());
        data.setScore(score);
        data.setId(record.getId());
        data.setProductName(pool.getProductName());
        data.setProductImgUrl(pool.getProductImgUrl());
        eventPublishService.addScore(this, userId, score, ScoreTypeEnum.ADD);
        return CjResult.success(data);
    }

    @Override
    public CjResult<NewPepoleActivityVo> newPeopleActivities(boolean loginFlag, Integer userId) {
        NewPepoleActivityVo vo= new NewPepoleActivityVo();

        //未登录状态，必出新人标识
        CjLotteryActivity activity = cjLotteryActivityDao.getNewPeopleActivities();
        if (activity == null) {
            vo.setNewPepoleFlag(false);
            return CjResult.success(vo);
        }
        if (!loginFlag){
            vo.setActivityCode(activity.getActivityCode());
            vo.setNewPepoleFlag(true);
            return CjResult.success(vo);
        }
        List<CjOrderPay> orderPays = cjOrderPayDao.selectByUserId(userId);
        if (!CollectionUtils.isEmpty(orderPays)) {
            orderPays = orderPays.stream().filter(s -> s.getStatus() != PayStatusEnum.NO_PAY.getCode()).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderPays)) {
                vo.setNewPepoleFlag(false);
                return CjResult.success(vo);
            }
        }

        vo.setNewPepoleFlag(true);
        vo.setActivityCode(activity.getActivityCode());
        return CjResult.success(vo);
    }

    @Override
    public CjResult<List<UserInfoVo>> getAwardwinningUserInfo(String activityCode) {
        List<CjLotteryRecord> cjLotteryRecords = lotteryRecordDao.selectNewestRecord();
        List<UserInfoVo> userInfoVos = Lists.newArrayList();
        //TODO 增加兜底方案，固定的弹幕数据
        if(CollectionUtils.isEmpty(cjLotteryRecords)){
          return CjResult.success(userInfoVos);
        }

        cjLotteryRecords.forEach(cjLotteryRecord -> {
            UserInfoVo infoVo = new UserInfoVo();
            Integer productId = cjLotteryRecord.getProductId();
            //商品信息
            if(productId != null){
                CjProductInfo cjProductInfo = productInfoDao.selectById(productId);
                CjProductInfoVo cjProductInfoVo = CjProductInfoMapper.INSTANCE.toVo(cjProductInfo);
                infoVo.setCjProductInfoVo(cjProductInfoVo);
            }
            //用户信息
            Integer customerId = cjLotteryRecord.getCustomerId();
            if(customerId != null){
                CjCustomerInfo cjCustomerInfo = userInfoService.queryUserInfoByCustomerId(customerId);
                if(cjCustomerInfo != null){
                    infoVo.setCustomerName(cjCustomerInfo.getCustomerName());
                    infoVo.setHeadUrl(cjCustomerInfo.getHeadUrl());
                    infoVo.setScore(cjCustomerInfo.getScore());
                }
            }
            userInfoVos.add(infoVo);
        });
        return CjResult.success(userInfoVos);
    }

    public CjPrizePool randomPrize(int activityId) {
        List<CjPrizePool> cjPrizePools = prizePoolDao.selectProductByActivityId(activityId);
        CjPrizePool pool = new CjPrizePool();
        if (CollectionUtils.isEmpty(cjPrizePools)) {
            //没有可以抽取的商品，兜底方案，固定返回一个商品

        } else {
            pool = cjPrizePools.get(this.randomData(cjPrizePools.size()));
        }
        CjProductInfo productInfo = productInfoDao.selectById(pool.getProductId());
        if (productInfo != null) {
            pool.setProductImgUrl(productInfo.getProductImgUrl());
            pool.setProductName(productInfo.getProductName());
        }
        return pool;
    }

    private int randomData(int size) {
        Random random = new Random();
        return random.nextInt(size);
    }

    /**
     * 订单金额通过一定的算法
     * 生成欧气值
     * 金额（元）/10 * 0.5~1.3
     * 保证返回的score 不为0
     *
     * @param totalFee
     * @return
     */
    private int getScore(int totalFee) {
        totalFee = totalFee / 100;
        if (totalFee == 1) {
            return 1;
        }
        Random random = new Random();
        int num = random.nextInt(9) + 5;
        int score = totalFee * num / (10 * 10);
        if (score == 0) {
            score = 1;
        }
        return score;
    }

    private LotteryData testLottery(int score, CjLotteryActivity activity) {

        CjPrizePool pool = this.randomPrize(activity.getId());
        LotteryData data = new LotteryData();
        data.setOutTradeNo("");
        data.setCallbackRate(activity.getActivityRate());
        data.setProductImgUrl(ObjectUtils.isEmpty(pool.getProductImgUrl()) ? "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa2.att.hudong.com%2F86%2F10%2F01300000184180121920108394217.jpg&refer=http%3A%2F%2Fa2.att.hudong.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1617438456&t=f5044b9b5f155d873bfa47abb52ac1e6" : pool.getProductImgUrl());
        data.setScore(score);
        return data;
    }

}
