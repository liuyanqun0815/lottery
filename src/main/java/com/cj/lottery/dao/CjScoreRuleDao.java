package com.cj.lottery.dao;

import com.cj.lottery.domain.common.CjScoreRule;

import java.util.List;

public interface CjScoreRuleDao {

    int insert(CjScoreRule record);

    int insertSelective(CjScoreRule record);

    int updateByPrimaryKeySelective(CjScoreRule record);

    List<CjScoreRule> selectAllRule();

}