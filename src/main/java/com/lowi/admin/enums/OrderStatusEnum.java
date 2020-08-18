package com.lowi.admin.enums;

import lombok.Getter;

/**
 * OrderStatusEnum.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :  订单状态枚举
 * @since : 2020/8/12 18:13
 */
@Getter
public enum OrderStatusEnum {
    //已取消
    DELAY(-1, "已取消"),
    //未支付
    NORMAL(0, "未支付"),
    //已支付
    PAY(1, "已支付"),
    ;

    public static String getStr(Integer status) {
        if (status == null) {
            return null;
        }
        for (OrderStatusEnum statusEnum : values()) {
            if (status.equals(statusEnum.getStatus())) {
                return statusEnum.getMsg();
            }
        }
        return null;
    }

    OrderStatusEnum(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private Integer status;
    private String msg;
}
