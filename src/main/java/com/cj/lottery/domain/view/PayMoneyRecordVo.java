package com.cj.lottery.domain.view;

import com.cj.lottery.domain.CjOrderPay;
import com.cj.lottery.domain.CjPayScoreRecord;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Api("充值记录")
@Data
public class PayMoneyRecordVo {

    /**
     * 人民币
     */
    @ApiModelProperty("人民币（分）")
    private Integer totalFee;

    /**
     * 订单状态，1已支付，2已退款
     */
    @ApiModelProperty("订单状态 1已支付，2已回收，3已核销")
    private Integer status;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    public static PayMoneyRecordVo DoToVo(CjOrderPay record){
        PayMoneyRecordVo vo = new PayMoneyRecordVo();
        vo.setTotalFee(record.getTotalFee());
        vo.setStatus(record.getStatus());
        vo.setCreateTime(record.getCreateTime());
        return vo;
    }
}
