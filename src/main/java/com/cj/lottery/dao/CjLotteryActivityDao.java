package com.cj.lottery.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cj.lottery.domain.CjLotteryActivity;
import com.cj.lottery.domain.common.ActivityUploadParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CjLotteryActivityDao {

    int insertSelective(CjLotteryActivity record);


    int updateByPrimaryKeySelective(CjLotteryActivity record);

    IPage<CjLotteryActivity> selectPageVo(Page<?> page,@Param("activity_flag") String activity_flag);


    CjLotteryActivity selectActivityByCode(@Param("code") String code);

    CjLotteryActivity getNewPeopleActivities();

    List<CjLotteryActivity> getAllActivities();


    List<CjLotteryActivity> selectByIdList(@Param("activityidList") List<Integer> activityidList);

    int batchSave(List<ActivityUploadParam> result);
}