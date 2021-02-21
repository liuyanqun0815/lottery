package com.cj.lottery.dao;

import com.cj.lottery.domain.CjProductCategory;

public interface CjProductCategoryDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CjProductCategory record);

    int insertSelective(CjProductCategory record);

    CjProductCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CjProductCategory record);

    int updateByPrimaryKey(CjProductCategory record);
}