package com.cj.lottery.domain.view;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class PageView<T> {


    private long size;

    private List<T> modelList = Lists.newArrayList();
}
