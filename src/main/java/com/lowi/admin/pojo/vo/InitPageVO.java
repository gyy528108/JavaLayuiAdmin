package com.lowi.admin.pojo.vo;

import lombok.Data;

/**
 * InitPageVO.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/8/17 14:21
 */
@Data
public class InitPageVO {
    private Integer authorityId;
    private String authorityName;
    private String menuUrl;
    private String menuIcon;
    private Integer parentId;
    private Boolean admin;
    private Boolean superAdmin;
    private Integer pageLevel;
}
