package com.cj.lottery.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cj.lottery.dao.CjLotteryActivityDao;
import com.cj.lottery.dao.CjLotteryActivityImgDao;
import com.cj.lottery.domain.CjLotteryActivity;
import com.cj.lottery.domain.CjLotteryActivityImg;
import com.cj.lottery.domain.view.PageView;
import com.cj.lottery.service.LotteryActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class LotteryActivityServiceImpl implements LotteryActivityService {

    @Autowired
    private CjLotteryActivityDao cjLotteryActivityDao;

    @Autowired
    private CjLotteryActivityImgDao cjLotteryActivityImgDao;

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
    public PageView queryActivityDetailsByPage(String activityCode,int current, int size) {

        CjLotteryActivity activity = cjLotteryActivityDao.selectActivityByCode(activityCode);
        PageView pageView = new PageView();
        if(null != activity){
            Integer id = activity.getId();
            //TODO 分页
            List<CjLotteryActivityImg> cjLotteryActivityImgs = cjLotteryActivityImgDao.listCjLotteryActivityImg(id);
           if(null != cjLotteryActivityImgs){
               pageView.setSize(cjLotteryActivityImgs.size());
               pageView.setModelList(cjLotteryActivityImgs);
           }
        }
        return pageView;
    }
}
