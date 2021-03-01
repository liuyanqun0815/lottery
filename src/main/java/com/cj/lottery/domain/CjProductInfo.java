package com.cj.lottery.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * cj_product_info
 * @author 
 */
@Data
public class CjProductInfo implements Serializable {
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
     * 分类id
     */
    private Integer categoryId;

    /**
     * 成本价格单位（分
     */
    private Integer price;

    /**
     * 销售价格（分）
     */
    private Integer salePrice;

    /**
     * 上下架状态：0上架1下架
     */
    private Integer status;

    /**
     * 商品有效期
     */
    private Date shelfLife;

    /**
     * 商品描述
     */
    private String desc;

    /**
     * 回收率，百分制，80
     */
    private Integer callbackRate;

    private String productImgUrl;
    /**
     * 商品标识：0 普通商品 1 隐藏商品
     */
    private Integer productFlag;
    private static final long serialVersionUID = 1L;
}