package com.cj.lottery.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cj.lottery.domain.CjPrizePool;
import com.cj.lottery.domain.CjProductInfo;
import com.cj.lottery.domain.view.CjProductInfoComplexVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CjProductInfoDao {


    int insertSelective(CjProductInfo record);


    int updateByPrimaryKeySelective(CjProductInfo record);

    CjProductInfo selectById(Integer id);

    List<CjProductInfo> selectByIds(@Param("productIds")List<Integer> productIds);

    List<CjPrizePool> selectPoolPrice();

    List<CjProductInfo> selectAll();

    int batchSave(@Param("list") List<CjProductInfo> list);

    IPage<CjProductInfoComplexVo> selectListProduct(Page<?> page, @Param("productName") String productName);


}