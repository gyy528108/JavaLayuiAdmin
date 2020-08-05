package com.lowi.admin.pojo.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * ProductDto.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/7/22 21:08
 */
@Data
public class ProductDto {
    private String productName;
    private String imgUrl;
    private Integer oneCategory;
    private Integer twoCategory;
    private Integer count;
    private String unit;
    private BigDecimal price;
    private String priceUnit;
    private String context;
    private String token;

    @Override
    public String toString() {
        return "ProductDto{" +
                "productName='" + productName + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", oneCategory=" + oneCategory +
                ", twoCategory=" + twoCategory +
                ", count=" + count +
                ", unit='" + unit + '\'' +
                ", price=" + price +
                ", priceUnit='" + priceUnit + '\'' +
                ", context='" + context + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
