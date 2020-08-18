package com.lowi.admin.dao;

import com.lowi.admin.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lowi.admin.pojo.dto.UserDto;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author Lowi
 * @since 2019-12-18
 */
@Repository
public interface UserDao extends BaseMapper<User> {

    /**
     * 用户登录
     * @param mobile 手机号
     * @param password 密码
     * @auther gengyy
     * @date 2020-08-17 9:52
     * @return 用户登录信息
     * */
    User loginUser(@Param("mobile") String mobile,@Param("password") String password);

    void updateLoginCount(UserDto user);
}
