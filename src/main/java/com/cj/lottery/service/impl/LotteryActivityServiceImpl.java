package com.cj.lottery.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cj.lottery.constant.ImgDomain;
import com.cj.lottery.dao.CjLotteryActivityDao;
import com.cj.lottery.dao.CjLotteryActivityImgDao;
import com.cj.lottery.dao.CjOrderPayDao;
import com.cj.lottery.dao.CjProductInfoDao;
import com.cj.lottery.domain.*;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.LotteryActivityInfoVo;
import com.cj.lottery.domain.view.PageView;
import com.cj.lottery.enums.ErrorEnum;
import com.cj.lottery.enums.ImgTypeEnum;
import com.cj.lottery.enums.PayStatusEnum;
import com.cj.lottery.service.LotteryActivityService;
import com.cj.lottery.service.UserInfoService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
public class LotteryActivityServiceImpl implements LotteryActivityService {

    @Autowired
    private CjLotteryActivityDao cjLotteryActivityDao;

    @Autowired
    private CjLotteryActivityImgDao cjLotteryActivityImgDao;

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private CjProductInfoDao cjProductInfoDao;
    @Autowired
    private CjOrderPayDao orderPayDao;


    @Override
    public PageView queryActivityListByPage(int current, int size,Integer userId) {
        String activity_flag = null;
        //判断是否拥有新人福利活动
        if (userId != null){
            List<CjOrderPay> orderPays = orderPayDao.selectByUserId(userId);
            if (!CollectionUtils.isEmpty(orderPays)) {
                Optional<CjOrderPay> first = orderPays.stream().filter(s -> s.getStatus() != PayStatusEnum.NO_PAY.getCode()).findFirst();
                if (first.isPresent()) {
                    activity_flag = "1";
                }
            }
        }
        PageView pageView = new PageView();
        Page<CjLotteryActivity> page = new Page<>(current, size);
        IPage<CjLotteryActivity> pageVo = cjLotteryActivityDao.selectPageVo(page,activity_flag);
        if (pageVo != null) {
            pageView.setSize(pageVo.getTotal());
            if (!CollectionUtils.isEmpty(pageVo.getRecords())) {
                pageVo.getRecords().forEach(s -> {
                    if (s.getActivityDeadline() == null) {
                        s.setActivityDeadlineFlag(false);
                    } else {
                        s.setActivityDeadlineFlag(true);
                    }
                });

                //不是新人的话替换第一张图片
                if (!ObjectUtils.isEmpty(activity_flag)) {
                    pageVo.getRecords().stream().skip(0).limit(1).forEach(s -> s.setActivityImg("h5_img/index/2_red.png"));
                }
                List<CjLotteryActivity> collect = pageVo.getRecords().stream().sorted((o1, o2) -> o2.getSort().compareTo(o2.getSort())).collect(Collectors.toList());
                collect.stream().forEach(s->s.setActivityImg(ImgDomain.imgUrlDomain+s.getActivityImg()));
                pageView.setModelList(collect);
            }
        }
        return pageView;
    }

    @Override
    public CjResult<LotteryActivityInfoVo> queryActivityDetailsByPage(int userId, String activityCode) {

        CjLotteryActivity activity = cjLotteryActivityDao.selectActivityByCode(activityCode);
        if (activity == null) {
            return CjResult.fail(ErrorEnum.NOT_ACITVITY);
        }
        LotteryActivityInfoVo infoVo = new LotteryActivityInfoVo();
        infoVo.setActivityName(activity.getActivityName());
        infoVo.setConsumerMoney(activity.getConsumerMoney());
        infoVo.setLimitTime(activity.getActivityDeadline() == null ? false : true);
        infoVo.setActivityDeadline(activity.getActivityDeadline() == null ? 0 : activity.getActivityDeadline().getTime());
        infoVo.setActivityFlag(activity.getActivityFlag());

        Integer id = activity.getId();
        List<CjLotteryActivityImg> cjLotteryActivityImgs = cjLotteryActivityImgDao.listCjLotteryActivityImg(id);
        if (!CollectionUtils.isEmpty(cjLotteryActivityImgs)) {
            Set<Integer> productSet = cjLotteryActivityImgs.stream().map(CjLotteryActivityImg::getProductId).collect(Collectors.toSet());
            List<CjProductInfo> cjProductInfos = cjProductInfoDao.selectByIds(Lists.newArrayList(productSet));
            Map<Integer, String> idNameMap = cjProductInfos.stream().collect(Collectors.toMap(CjProductInfo::getId, CjProductInfo::getDesc));
            List<String> headUrls = cjLotteryActivityImgs.stream().
                    filter(s -> ImgTypeEnum.LUN_BO.getCode() == s.getType()).
                    sorted((o1, o2) -> o2.getSort().compareTo(o1.getSort())).
                    map(s -> ImgDomain.imgUrlDomain+ s.getImgUrl()).collect(Collectors.toList());
            List<ImgNameVo> imgNameVos = Lists.newArrayList();
            cjLotteryActivityImgs = cjLotteryActivityImgs.stream().
                    filter(s -> ImgTypeEnum.IMG_BODY.getCode() == s.getType()).sorted((o1, o2) -> o2.getSort().compareTo(o1.getSort())).collect(Collectors.toList());
            cjLotteryActivityImgs.stream().forEach(s -> {
                        ImgNameVo vo = new ImgNameVo();
                        vo.setImgUrl(ImgDomain.imgUrlDomain+s.getImgUrl());
                        vo.setProductName(idNameMap.get(s.getProductId()));
                        imgNameVos.add(vo);
                    }
            );
            infoVo.setHeadUrlList(headUrls);
            infoVo.setBodyUrlList(imgNameVos);
        }

        CjCustomerInfo cjCustomerInfo = userInfoService.queryUserInfoByCustomerId(userId);
        if (null != cjCustomerInfo) {
            infoVo.setScore(cjCustomerInfo.getScoreInFen());
        }
        return CjResult.success(infoVo);
    }
}
