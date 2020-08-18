package com.lowi.admin.pojo.vo;

import com.lowi.admin.entity.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;

/**
 * UserVo.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/7/21 15:36
 */
@Data
public class UserVo extends User {
    private String sexStr;

    public static UserVo fromVo(User user) {
        if (user == null) {
            return null;
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        if (user.getSex() != null) {
            if (user.getSex() == 0) {
                userVo.setSexStr("女");
            } else {
                userVo.setSexStr("男");
            }
        }
        return userVo;
    }

    @Override
    public String toString() {
        super.toString();
        return "UserVo{" +
                "sexStr='" + sexStr + '\'' +
                '}';
    }
}
