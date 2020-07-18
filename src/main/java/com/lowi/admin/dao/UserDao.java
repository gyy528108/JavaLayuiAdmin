package com.lowi.admin.dao;

import com.lowi.admin.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lowi.admin.pojo.dto.UserDto;
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

    User loginUser(String mobile, String password);

    void updateLoginCount(UserDto user);
}
