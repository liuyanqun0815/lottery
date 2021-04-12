package com.cj.lottery.dao;

import com.cj.lottery.domain.CjSendProduct;

public interface CjSendProductDao {

    int insertSelective(CjSendProduct record);


    int updateByPrimaryKeySelective(CjSendProduct record);

}