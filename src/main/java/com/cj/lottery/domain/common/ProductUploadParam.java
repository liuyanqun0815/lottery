package com.cj.lottery.domain.common;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class ProductUploadParam implements java.io.Serializable {

    /**
     * 商品名称
     */
    @Excel(name = "产品名称", width = 25,orderNum="1")
    private String productName;

    /**
     * 商品描述
     */

    @Excel(name = "详情页名称", width = 25,orderNum = "6")
    private String desc;

    /**
     * 回收率，百分制，70
     */
    private Integer callbackRate;

    @Excel(name = "产品图片", width = 25,orderNum = "2")
    private String productImgUrl;
    /**
     * 商品标识：0 普通商品 1 隐藏商品
     */
    @Excel(name = "是否隐藏产品", replace = {"是_1", "否_0"},orderNum = "3")
    private Integer productFlag;

    @Excel(name = "详情页显示排序（新字段）",orderNum = "5")
    private int sort;

    @Excel(name = "（新字段）简称",orderNum = "4")
    private String simpleName;



    @Excel(name = "（新字段）活动页详情图",orderNum = "10")
    private String activityInfoUrl;

    @Excel(name = "（新字段）活动页详情图轮播图",orderNum = "8")
    private String lunboUrl;

    @Excel(name = "属于哪个活动（活动名称）",orderNum = "9")
    private String activityName;

    @Excel(name = "（新字段）中奖率（0不会中 1会中）", replace = {"是_1", "否_0"},orderNum = "3")
    private Integer getFlag;
}
