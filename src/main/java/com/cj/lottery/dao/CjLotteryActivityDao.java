package com.cj.lottery.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cj.lottery.domain.CjLotteryActivity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CjLotteryActivityDao {

    int insertSelective(CjLotteryActivity record);


    int updateByPrimaryKeySelective(CjLotteryActivity record);

    IPage<CjLotteryActivity> selectPageVo(Page<?> page);


    CjLotteryActivity selectActivityByCode(@Param("code") String code);

    List<CjLotteryActivity> getNewPeopleActivities();

}