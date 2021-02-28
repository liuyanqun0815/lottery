package com.cj.lottery.mapper;

import com.cj.lottery.domain.CjCustomerAddress;
import com.cj.lottery.domain.view.ConstumerAddressInfoVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ConstumerAddressInfoMapper {

    ConstumerAddressInfoMapper INSTANCE = Mappers.getMapper(ConstumerAddressInfoMapper.class);

    ConstumerAddressInfoVo toVo(CjCustomerAddress cjCustomerAddress);

    CjCustomerAddress toDo(ConstumerAddressInfoVo constumerAddressInfoVo);

}
