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
    private Byte status;

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

    /**
     * 删除状态，默认0，1删除
     */
    private Byte isDelete;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}