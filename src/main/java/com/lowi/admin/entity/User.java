package com.lowi.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author Lowi
 * @since 2019-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 是否是超级管理员
     */
    private Boolean isSuperAdmin;
    /**
     * 父id
     */
    private Integer parentId;

    /**
     * 用户名
     */
    private String username;

    private String headImg;

    private Integer sex;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 注册时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是否删除（0：正常；1：已删）
     */
    private Boolean isDel;

    /**
     * 更新版本
     */
    private Integer version;

    /**
     * 登录次数
     */
    private Integer loginCount;
    /**
     * 登录ip
     */
    private String loginIp;

    /**
     * 上次登录ip
     */
    private String lastLoginIp;
}
