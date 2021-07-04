package com.cj.lottery.dao.simple;

import com.cj.lottery.domain.simple.CjSimpleSendProduct;

public interface CjSimpleSendProductDao {

    int insertSelective(CjSimpleSendProduct record);


    int updateByPrimaryKeySelective(CjSimpleSendProduct record);

}