package com.lowi.admin.controller;

import com.lowi.admin.pojo.dto.UserPageDTO;
import com.lowi.admin.service.PageIconService;
import com.lowi.admin.service.UserPageService;
import com.lowi.admin.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserPageController.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/8/17 14:56
 */
@RestController
@RequestMapping("userPage")
public class UserPageController {
    @Autowired
    UserPageService userPageService;
    @Autowired
    PageIconService pageIconService;

    @RequestMapping("/getAllPage")
    public Result getAllPage(String token) {
        return userPageService.getAllPage(token);
    }

    @RequestMapping("/addPage")
    public Result addPage(@RequestBody UserPageDTO userPageDTO) {
        return userPageService.addPage(userPageDTO);
    }

    @RequestMapping("/getAllIcon")
    public Result getAllIcon(String token) {
        return pageIconService.getAllIcon(token);
    }

    @RequestMapping("/getTwoLevelMenu")
    public Result getTwoLevelMenu(String token) {
        return userPageService.getTwoLevelMenu(token);
    }

    @RequestMapping("/editUserPage")
    public Result editUserPage(@RequestBody UserPageDTO userPageDTO) {
        return userPageService.editUserPage(userPageDTO);
    }
}
