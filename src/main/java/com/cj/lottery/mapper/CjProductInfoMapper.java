package com.cj.lottery.mapper;

import com.cj.lottery.domain.CjSendProduct;
import com.cj.lottery.domain.view.CjProductInfoVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CjProductInfoMapper {

    CjProductInfoMapper INSTANCE = Mappers.getMapper(CjProductInfoMapper.class);

    CjProductInfoVo toVo(CjSendProduct cjSendProduct);

    CjSendProduct toDo(CjProductInfoVo cjProductInfoVo);

    List<CjProductInfoVo> toVos(List<CjSendProduct> cjSendProducts);

    List<CjSendProduct> toDos(List<CjProductInfoVo> cjProductInfoVos);
}
