package com.lowi.admin.pojo;

import lombok.Data;

import java.util.List;

/**
 * MenuInfoVO.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc : 页面类
 * @since : 2020/8/17 10:12
 */
@Data
public class MenuInfoVO {
    private List<MenuInfoVO> child;
    private String href;
    private String icon;
    private String target;
    private String title;
}
