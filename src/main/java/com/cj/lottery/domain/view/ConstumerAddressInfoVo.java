package com.cj.lottery.domain.view;

import com.cj.lottery.domain.CjCustomerAddress;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
@Api("收货地址")
public class ConstumerAddressInfoVo {


    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("收货姓名")
    private String name;
    @ApiModelProperty("收货手机")
    private String phone;

    @ApiModelProperty("地址")
    private String address;

    public static ConstumerAddressInfoVo DoToVo(CjCustomerAddress address){
        ConstumerAddressInfoVo vo = new ConstumerAddressInfoVo();
        BeanUtils.copyProperties(address,vo);
        return vo;
    }

    public static CjCustomerAddress VoToDo(ConstumerAddressInfoVo addressInfoVo){
        CjCustomerAddress domain = new CjCustomerAddress();
        BeanUtils.copyProperties(addressInfoVo,domain);
        return domain;
    }
}
