package com.cj.lottery.domain.view;

import com.cj.lottery.domain.CjLotteryActivity;
import lombok.Data;

@Data
public class EnumVo {

    private String label;

    private String value;


    public static EnumVo toVo(CjLotteryActivity activity){
        EnumVo vo = new EnumVo();
        vo.setLabel(activity.getActivityName());
        vo.setValue(activity.getActivityCode());

//        vo.setLable(activity.getActivityCode());
//        vo.setValue(activity.getActivityName());
        return vo;
    }
}
