package com.lowi.admin.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Md5Utils.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2019/12/19 15:35
 */
@Component
public class Md5Utils {

    @PostConstruct
    public void beforeInit() {
        plaintextStr = plaintext;
    }

    @Value("${plaintext}")
    private String plaintext;

    private static String plaintextStr;

    public static String md5Hex(String password) {
        String newPassword = DigestUtils.md5Hex(DigestUtils.md5Hex(password) + plaintextStr);
        return newPassword;
    }
}
