package com.lowi.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lowi.admin.dao.UserDao;
import com.lowi.admin.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * TestService.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2019/12/18 16:03
 */
@Service
public class TestService {
    @Autowired
    private UserDao userMapper;


    public User findUserByMobile(String phone){
        User selectOne = new User();
        selectOne.setMobile(phone);
        User user = userMapper.selectOne(new QueryWrapper<>(selectOne));
        return user;
    }
    public User getUser() {
        User selectOne = new User();
        selectOne.setId(1);
        User user = userMapper.selectOne(new QueryWrapper<>(selectOne));
        return user;
    }
}
