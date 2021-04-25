package com.cj.lottery.service;

import com.cj.lottery.dao.CjCustomerInfoDao;
import com.cj.lottery.dao.CjScoreRuleDao;
import com.cj.lottery.domain.CjCustomerInfo;
import com.cj.lottery.domain.common.CjScoreRule;
import com.cj.lottery.enums.RuleOperatorTypeEnum;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Slf4j
@Service
public class ScoreRuleService {

    @Autowired
    private CjScoreRuleDao scoreRuleDao;
    @Autowired
    private CjCustomerInfoDao customerInfoDao;


    public LoadingCache<Long, List<CjScoreRule>> loadingCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(60, TimeUnit.SECONDS)
            .build(new CacheLoader<Long, List<CjScoreRule>>() {
                @Override
                public List<CjScoreRule> load(Long key) throws Exception {
                    return selectAllRule();
                }
            });


    /**
     * 通过规则
     * 获取欧气值
     *
     * @param userId
     * @return
     */
    public String getScore(int userId) {

        CjCustomerInfo info = customerInfoDao.selectByCustomerId(userId);
        if (info == null) {
            return "0";
        }
        int payCount = info.getPayCount();
        List<CjScoreRule> cjScoreRules = null;
        try {
            cjScoreRules = loadingCache.get(0L);
        } catch (ExecutionException e) {
            log.info("getScore method exception:", e);
        }
        for (CjScoreRule rule : cjScoreRules) {
            RuleOperatorTypeEnum operatorTypeEnum = RuleOperatorTypeEnum.parse(rule.getOperate());
            String[] args = rule.getPayCount().split(",");
            boolean result = false;
            switch (operatorTypeEnum) {
                case EQUALS:
                    result = StringUtils.equals(String.valueOf(payCount), rule.getPayCount());
                    break;
                case NOT_EQUALS:
                    result = !StringUtils.equals(String.valueOf(payCount), rule.getPayCount());
                    break;
                case GREATER_THAN:
                    result = Long.valueOf(payCount) > Long.valueOf(rule.getPayCount());
                    break;
                case LESS_THEN:
                    result = Long.valueOf(payCount) < Long.valueOf(rule.getPayCount());
                    break;
                case GREATER_EQUALS_THAN:
                    result = Long.valueOf(payCount) >= Long.valueOf(rule.getPayCount());
                    break;
                case LESS_EQUALS_THEN:
                    result = Long.valueOf(payCount) <= Long.valueOf(rule.getPayCount());
                    break;
                case CONTAIN:

                    //这里要有所有外部的接口
//                    if (StringUtils.isNotEmpty(condition.getExternDataSource()) && beanMap.containsKey(condition.getExternDataSource())) {
//                        result = beanMap.get(condition.getExternDataSource()).validate(operatorTypeEnum, source, condition.getCollectionId(), condition.getFieldName());
//                        break;
//                    }
//                    result = sets.contains(source);
//                    break;
                case NOT_COTAIN:

                    //这里要有所有外部的接口
//                    if (StringUtils.isNotEmpty(condition.getExternDataSource()) && beanMap.containsKey(condition.getExternDataSource())) {
//                        result = beanMap.get(condition.getExternDataSource()).validate(operatorTypeEnum, source, condition.getCollectionId(), condition.getFieldName());
//                        break;
//                    }
//
//                    result = !sets.contains(source);
//                    break;
                case BETWEEN:
                    List<Long> list = Lists.newArrayList(args).stream().map(Long::valueOf).sorted().collect(Collectors.toList());
                    Range<Long> range = Range.closed(list.get(0), list.get(1));
                    long sourceValue = Long.valueOf(payCount);
                    result = range.contains(sourceValue);
                    break;
                case EXPRESSION_MATCH:
                    result = Pattern.matches(rule.getPayCount(), payCount + "");
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            if (result) {
                return rule.getScore() ;
            }
        }
        return "0";
    }


    public List<CjScoreRule> selectAllRule() {
        return scoreRuleDao.selectAllRule();
    }
}
