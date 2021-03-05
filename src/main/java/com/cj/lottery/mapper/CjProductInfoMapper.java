package com.cj.lottery.mapper;

import com.cj.lottery.domain.CjProductInfo;
import com.cj.lottery.domain.view.CjProductInfoVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CjProductInfoMapper {

    CjProductInfoMapper INSTANCE = Mappers.getMapper(CjProductInfoMapper.class);

    CjProductInfoVo toVo(CjProductInfo cjProductInfo);

    CjProductInfo toDo(CjProductInfoVo cjProductInfoVo);

    List<CjProductInfoVo> toVos(List<CjProductInfo> cjProductInfos);

    List<CjProductInfo> toDos(List<CjProductInfo> cjProductInfoVos);
}
