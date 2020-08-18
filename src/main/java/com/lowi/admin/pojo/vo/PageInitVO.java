package com.lowi.admin.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * PageInitVO.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :   页面初始化类
 * @since : 2020/8/17 10:06
 */
@Data
public class PageInitVO {
    private Object homeInfo;
    private Object logoInfo;
    private List<Object> menuInfo;
}
