package com.cj.lottery.dao;

import com.cj.lottery.domain.CjSendProduct;

public interface CjSendProductDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CjSendProduct record);

    int insertSelective(CjSendProduct record);

    CjSendProduct selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CjSendProduct record);

    int updateByPrimaryKey(CjSendProduct record);
}