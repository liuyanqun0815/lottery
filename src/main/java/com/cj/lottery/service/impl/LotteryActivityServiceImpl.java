package com.cj.lottery.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cj.lottery.dao.CjLotteryActivityDao;
import com.cj.lottery.dao.CjLotteryActivityImgDao;
import com.cj.lottery.domain.CjCustomerInfo;
import com.cj.lottery.domain.CjLotteryActivity;
import com.cj.lottery.domain.CjLotteryActivityImg;
import com.cj.lottery.domain.view.CjResult;
import com.cj.lottery.domain.view.LotteryActivityInfoVo;
import com.cj.lottery.domain.view.PageView;
import com.cj.lottery.enums.ActivityFlagEnum;
import com.cj.lottery.enums.ErrorEnum;
import com.cj.lottery.service.LotteryActivityService;
import com.cj.lottery.service.UserInfoService;
import com.cj.lottery.util.ContextUtils;
import com.cj.lottery.util.DateUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
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

    @Override
    public PageView queryActivityListByPage(int current, int size) {
        PageView pageView = new PageView();
        Page<CjLotteryActivity> page = new Page<>(current, size);
        IPage<CjLotteryActivity> pageVo = cjLotteryActivityDao.selectPageVo(page);
        if (pageVo != null) {
            pageView.setSize(pageVo.getTotal());
            if (!CollectionUtils.isEmpty(pageVo.getRecords())){
                pageVo.getRecords().forEach(s->{
                    if (s.getActivityDeadline() == null){
                        s.setActivityDeadlineFlag(false);
                    }else {
                        s.setActivityDeadlineFlag(true);
                    }

                });
                pageView.setModelList(pageVo.getRecords());

            }
        }
        return pageView;
    }

    @Override
    public CjResult<LotteryActivityInfoVo> queryActivityDetailsByPage(int userId,String activityCode) {

        CjLotteryActivity activity = cjLotteryActivityDao.selectActivityByCode(activityCode);
        if (activity == null) {
            return CjResult.fail(ErrorEnum.NOT_ACITVITY);
        }
        LotteryActivityInfoVo lotteryActivityInfoVo = new LotteryActivityInfoVo();
        lotteryActivityInfoVo.setActivityCode(activityCode);
        String format = DateUtil.stringFormat(activity.getActivityDeadline(), DateUtil.YYYY_MM_DD_HH_MM_SS);
        lotteryActivityInfoVo.setActivityDeadline(activity.getActivityDeadline().getTime());
        lotteryActivityInfoVo.setConsumerMoney(activity.getConsumerMoney());
        lotteryActivityInfoVo.setLimitTime(activity.getActivityDeadline() == null ? false : true);
        lotteryActivityInfoVo.setActivityFlag(activity.getActivityFlag());

        Integer id = activity.getId();
        List<CjLotteryActivityImg> cjLotteryActivityImgs = cjLotteryActivityImgDao.listCjLotteryActivityImg(id);
        if (!CollectionUtils.isEmpty(cjLotteryActivityImgs)) {
            List<String> headUrls = cjLotteryActivityImgs.stream().
                    filter(s -> 1==s.getType()).map(s -> s.getImgUrl()).collect(Collectors.toList());
            List<String> bodyUrls = cjLotteryActivityImgs.stream().
                    filter(s -> 2==s.getType()).map(s -> s.getImgUrl()).collect(Collectors.toList());

            lotteryActivityInfoVo.setHeadUrlList(headUrls);
            lotteryActivityInfoVo.setBodyUrlList(bodyUrls);
        }


        CjCustomerInfo cjCustomerInfo = userInfoService.queryUserInfoByCustomerId(userId);
        if (null != cjCustomerInfo) {
            lotteryActivityInfoVo.setScore(cjCustomerInfo.getScore());
        }
        return CjResult.success(lotteryActivityInfoVo);
    }
}
