package com.cj.lottery.dao;

import com.cj.lottery.domain.CjMerchant;

import java.util.List;

public interface CjMerchantDao {

    int updateByPrimaryKeySelective(CjMerchant record);

    List<CjMerchant> selelctAllMerchant();

    int insert(String channel);



}