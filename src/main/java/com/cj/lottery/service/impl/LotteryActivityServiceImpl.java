package com.cj.lottery.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cj.lottery.dao.CjLotteryActivityDao;
import com.cj.lottery.domain.CjLotteryActivity;
import com.cj.lottery.domain.view.PageView;
import com.cj.lottery.service.LotteryActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class LotteryActivityServiceImpl implements LotteryActivityService {

    @Autowired
    CjLotteryActivityDao cjLotteryActivityDao;

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
}
