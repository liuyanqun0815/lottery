package com.cj.lottery.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cj.lottery.domain.CjPrizePool;
import com.cj.lottery.domain.view.ProductPoolVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CjPrizePoolDao {


    int insertSelective(CjPrizePool record);

    int updateByPrimaryKeySelective(CjPrizePool record);

    List<CjPrizePool> selectProductByActivityId(int activityId );

    int subtractionProductNum(@Param("id") Integer id, @Param("version") String version);

    /**
     * 获取可用的
     * @return
     */
    List<CjPrizePool> selectAllProduct();

    List<CjPrizePool> selectAllProductByActivityId(int activityId );

    IPage<ProductPoolVo> selectPagePool(Page<?> page, @Param("productName") String productName,
                                        @Param("activityCode") String activityCode,
                                         @Param("status") Integer status,@Param("used") Integer used);


}