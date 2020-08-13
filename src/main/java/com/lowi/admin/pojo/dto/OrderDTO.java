package com.lowi.admin.pojo.dto;

import lombok.Data;

/**
 * OrderDTO.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/8/12 17:46
 */
@Data
public class OrderDTO {
    private Integer id;
    private Integer productId;
    private String token;
    private String orderId;
}
