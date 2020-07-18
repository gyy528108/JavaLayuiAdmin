package com.lowi.admin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * ProfileConfig.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/6/18 13:47
 */
@Configuration
public class ProfileConfig {
    @Autowired
    private ApplicationContext context;

    private static ApplicationContext contextStatic;

    @PostConstruct
    public void beforeInit() {
        contextStatic = context;
    }


    public static String getActiveProfile() {
        return contextStatic.getEnvironment().getActiveProfiles()[0];
    }
}
