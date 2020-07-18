package com.lowi.admin.utils;

import cn.hutool.core.date.DateUtil;

/**
 * TokenUtils.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/6/28 16:07
 */
public class TokenUtils {

    public static String createToken(Integer userId, String seconds) {
        String token = "Lowi" + userId + seconds;
        String md5Hex = Md5Utils.md5Hex(token);
        return md5Hex;
    }
}
