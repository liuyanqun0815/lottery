package com.cj.lottery.domain.view;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NewPepoleActivityVo {

    @ApiModelProperty("新人活动编码")
    private String activityCode;

    @ApiModelProperty("新人活动标识,true 标识新人，false 否")
    private boolean newPepoleFlag;
}
