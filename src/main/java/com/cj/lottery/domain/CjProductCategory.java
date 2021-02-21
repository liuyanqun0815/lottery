package com.cj.lottery.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * cj_product_category
 * @author 
 */
@Data
public class CjProductCategory implements Serializable {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 分类编码
     */
    private String categoryCode;

    /**
     * 父分类id
     */
    private Integer parentId;

    /**
     * 根节点id
     */
    private Integer rootId;

    /**
     * 分类层级
     */
    private Integer categoryLevel;

    /**
     * 分类状态 0有效，1无效
     */
    private Byte status;

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