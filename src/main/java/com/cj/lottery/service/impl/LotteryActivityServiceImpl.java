package com.cj.lottery.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cj.lottery.dao.CjLotteryActivityDao;
import com.cj.lottery.dao.CjLotteryActivityImgDao;
import com.cj.lottery.domain.CjCustomerInfo;
import com.cj.lottery.domain.CjLotteryActivity;
import com.cj.lottery.domain.CjLotteryActivityImg;
import com.cj.lottery.domain.view.LotteryActivityInfoVo;
import com.cj.lottery.domain.view.PageView;
import com.cj.lottery.service.LotteryActivityService;
import com.cj.lottery.service.UserInfoService;
import com.cj.lottery.util.ContextUtils;
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
    private UserInfoService  userInfoService;

    @Override
    public PageView queryActivityListByPage(int current,int size) {
        PageView pageView = new PageView();
        Page<CjLotteryActivity> page = new Page<>(current,size);
        IPage<CjLotteryActivity> pageVo = cjLotteryActivityDao.selectPageVo(page);
        if (pageVo != null){
            pageView.setSize(pageVo.getTotal());
            pageView.setModelList(pageVo.getRecords());
        }
        return pageView;
    }

    @Override
    public LotteryActivityInfoVo queryActivityDetailsByPage(String activityCode, int current, int size) {

        CjLotteryActivity activity = cjLotteryActivityDao.selectActivityByCode(activityCode);

        LotteryActivityInfoVo lotteryActivityInfoVo = new LotteryActivityInfoVo();
        lotteryActivityInfoVo.setActivityCode(activityCode);
        if(null != activity){

            lotteryActivityInfoVo.setActivityDeadline(activity.getActivityDeadline());
            lotteryActivityInfoVo.setConsumerNum(activity.getConsumerNum());
            lotteryActivityInfoVo.setLimitTime(activity.getActivityDeadline() == null? false:true);
            lotteryActivityInfoVo.setActivityFlag(activity.getActivityFlag());

            Integer id = activity.getId();
            //TODO 分页
            List<CjLotteryActivityImg> cjLotteryActivityImgs = cjLotteryActivityImgDao.listCjLotteryActivityImg(id);
            if(!CollectionUtils.isEmpty(cjLotteryActivityImgs)){
                List<String> headUrls = cjLotteryActivityImgs.stream().
                        filter(s -> "1".equals(s.getType())).map(s->s.getImgUrl()).collect(Collectors.toList());
                List<String> bodyUrls = cjLotteryActivityImgs.stream().
                        filter(s -> "0".equals(s.getType())).map(s->s.getImgUrl()).collect(Collectors.toList());

                lotteryActivityInfoVo.setHeadUrlList(headUrls);
                lotteryActivityInfoVo.setBodyUrlList(bodyUrls);
            }
        }

        //获取用户欧气值
        int userId = ContextUtils.getUserId();
        CjCustomerInfo cjCustomerInfo = userInfoService.queryUserInfoByCustomerId(userId);
        if(null != cjCustomerInfo){
            lotteryActivityInfoVo.setScore(cjCustomerInfo.getScore());
        }
        return lotteryActivityInfoVo;
    }
}
