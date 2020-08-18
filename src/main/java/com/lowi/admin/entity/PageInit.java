package com.lowi.admin.entity;

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
 * @since 2020-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PageInit implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * admin账户是否能看 1 能 0 不能
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
