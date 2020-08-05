package com.lowi.admin.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author Lowi
 * @since 2020-07-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String productName;

    private String img;

    private Integer oneCategory;

    private Integer twoCategory;

    private String unit;

    private String productContent;

    private Integer total;

    private BigDecimal price;

    private String priceUnit;

    private Integer status;

    private Integer createId;

    private Integer parentId;

    private String createName;

    private Date createTime;


}
