package com.cj.lottery.dao.simple;

import com.cj.lottery.domain.simple.CjSimpleLotteryActivity;
import com.cj.lottery.domain.simple.view.SimpleActivityVo;

import java.util.List;

public interface CjSimpleLotteryActivityDao {


    int insertSelective(CjSimpleLotteryActivity record);


    int updateByPrimaryKeySelective(CjSimpleLotteryActivity record);

    CjSimpleLotteryActivity selectByCode(String activityCode);

    List<CjSimpleLotteryActivity> selectAll();
}