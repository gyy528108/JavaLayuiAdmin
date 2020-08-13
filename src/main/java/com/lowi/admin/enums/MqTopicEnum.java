package com.lowi.admin.enums;

import lombok.Getter;

/**
 * MqTopicEnum.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/8/12 17:56
 */
@Getter
public enum MqTopicEnum {
    //订单
    ORDER("order"),
    //支付
    PAY("pay"),
    //取消
    DELAY("delay"),
    ;

    MqTopicEnum(String topic) {
        this.topic = topic;
    }

    private String topic;
}
