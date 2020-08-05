package com.lowi.admin.pojo.dto;

import lombok.Data;

/**
 * EmailDto.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/5/20 16:25
 */
@Data
public class EmailDto {
    private String emailAddress;
    private String title;
    private String content;
    private String text;

    @Override
    public String toString() {
        return "EmailDto{" +
                "emailAddress='" + emailAddress + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
