package com.cj.lottery.domain.view;

import com.cj.lottery.domain.CjPayNiuniuRecord;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Api("扭扭币充值记录")
@Data
public class PayNiuniuRecordVo {

    /**
     * 扭扭币数量
     */
    @ApiModelProperty("扭扭币数量")
    private Integer niuniuNum;

    /**
     * 订单状态，1已支付，2已退款
     */
    @ApiModelProperty("订单状态")
    private Integer status;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    public static PayNiuniuRecordVo DoToVo(CjPayNiuniuRecord record){
        PayNiuniuRecordVo vo = new PayNiuniuRecordVo();
        BeanUtils.copyProperties(record,vo);
        return vo;
    }
}
