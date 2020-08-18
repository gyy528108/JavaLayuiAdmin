package com.lowi.admin.pojo.dto;

import lombok.Data;

import java.util.Date;

/**
 * UserPageDTO.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/8/17 14:58
 */
@Data
public class UserPageDTO {

    private String token;

    private Boolean isSuperAdmin;
    /**
     * 是否只有admin账户查看 0 否 1 是
     */
    private Boolean isAdmin;

    /**
     * 等级 0 最高等级
     */
    private Integer level;

    /**
     * 父id
     */
    private Integer parentId;

    /**
     * 跳转
     */
    private String href;

    /**
     * 图标
     */
    private String icon;

    /**
     * 跳转路径
     */
    private String target;

    /**
     * 标签
     */
    private String title;

    /**
     * 创建时间
     */
    private Date createTime;

}
