package com.lowi.admin.pojo.dto;

import lombok.Data;

/**
 * CategoryDto.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/7/23 16:15
 */
@Data
public class CategoryDto {

    private String token;

    private Integer parentId;

    @Override
    public String toString() {
        return "CategoryDto{" +
                "token='" + token + '\'' +
                ", parentId=" + parentId +
                '}';
    }
}
