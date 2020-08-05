package com.lowi.admin.pojo.vo;

import lombok.Data;

/**
 * WeLinkAccountVo.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/7/10 9:37
 */
@Data
public class FeishuAccountVo {
    private Integer companyId;
    private Integer userId;
    private String feiShuMiniProgramUserId;
    private String feiShuMiniProgramOpenId;
}
