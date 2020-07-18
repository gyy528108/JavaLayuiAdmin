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
 * 
 * </p>
 *
 * @author Lowi
 * @since 2020-05-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ip_data")
public class IpData implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 运营商
     */
    private String operator;

    /**
     * ip段
     */
    private String ipStart;

    /**
     * ip段
     */
    private String ipEnd;

    /**
     * ip转化为数字 范围的开始
     */
    private Long ipScopeStart;

    /**
     * ip转化为数字 范围的结束
     */
    private Long ipScopeEnd;

    /**
     * 是否删除 0 否 1是
     */
    private Integer isDel;

    /**
     * 创建时间
     */
    private Date createTime;


}
