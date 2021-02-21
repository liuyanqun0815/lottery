package com.cj.lottery.service.impl;

import com.cj.lottery.dao.CjProductInfoDao;
import com.cj.lottery.service.ProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductInfoServiceImpl implements ProductInfoService {
    @Autowired
    private CjProductInfoDao productInfoDao;


}
