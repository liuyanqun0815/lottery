package com.cj.lottery.domain.simple;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * cj_simple_product_info
 * @author 
 */
@Data
public class CjSimpleProductInfo implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 商品编码
     */
    private String productCode;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品描述
     */
    private String desc;

    /**
     * 活动id
     */
    private Integer activityId;

    /**
     * 上下架状态：0上架1下架
     */
    private Integer status;

    /**
     * 商品图片url
     */
    private String productImgUrl;

    private Integer luck;

    private int sort;


    private static final long serialVersionUID = 1L;
}