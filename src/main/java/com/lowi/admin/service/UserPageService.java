package com.lowi.admin.service;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lowi.admin.dao.PageInitDao;
import com.lowi.admin.entity.PageIcon;
import com.lowi.admin.entity.PageInit;
import com.lowi.admin.entity.User;
import com.lowi.admin.pojo.dto.UserPageDTO;
import com.lowi.admin.pojo.vo.InitPageVO;
import com.lowi.admin.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * UserPageService.java
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
@Service
public class UserPageService {
    @Autowired
    PageInitDao pageInitDao;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    final String MENU_LEVEL_KEY = "menu_level_";

    public Result getAllPage(String token) {
        String userInfo = stringRedisTemplate.opsForValue().get(token);
        if (userInfo == null) {
            return Result.getInstance(1, "请登录");
        }
        User userVo = JSONUtil.toBean(userInfo, User.class);
        if (!userVo.getIsSuperAdmin()) {
            return Result.getInstance(1, "无权限");
        }
        List<PageInit> pageInits = pageInitDao.selectList(null);
        List<InitPageVO> initPageVOS = new ArrayList<>();
        for (PageInit page : pageInits) {
            InitPageVO initPageVO = new InitPageVO();
            initPageVO.setAuthorityId(page.getId());
            initPageVO.setAuthorityName(page.getTitle());
            initPageVO.setMenuIcon(page.getIcon());
            initPageVO.setMenuUrl(page.getHref());
            initPageVO.setParentId(page.getParentId());
            initPageVO.setPageLevel(page.getLevel());
            initPageVO.setSuperAdmin(page.getIsSuperAdmin());
            initPageVO.setAdmin(page.getIsAdmin());
            initPageVOS.add(initPageVO);
        }

        return Result.getInstance(0, "成功", pageInits.size(), initPageVOS);
    }

    @Transactional(rollbackFor = Exception.class)
    public Result addPage(UserPageDTO userPageDTO) {
        String userInfo = stringRedisTemplate.opsForValue().get(userPageDTO.getToken());
        if (userInfo == null) {
            return Result.getInstance(1, "请登录");
        }
        User userVo = JSONUtil.toBean(userInfo, User.class);
        if (!userVo.getIsSuperAdmin()) {
            return Result.getInstance(1, "无权限");
        }
        return Result.getInstance(0, "");
    }

    public Result getTwoLevelMenu(String token) {
        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(MENU_LEVEL_KEY + "1");
        if (entries.size() > 0) {
            return Result.getInstance(0, "获取成功", entries);
        }
        QueryWrapper<PageInit> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("level", 1);
        List<PageInit> pageInits = pageInitDao.selectList(queryWrapper);
        for (PageInit pageInit : pageInits) {
            stringRedisTemplate.opsForHash().put(MENU_LEVEL_KEY + "1", String.valueOf(pageInit.getId()), pageInit.getTitle());
        }
        Map<Object, Object> menuLevel = stringRedisTemplate.opsForHash().entries(MENU_LEVEL_KEY + "1");
        return Result.getInstance(0, "获取成功", menuLevel);
    }

    public Result editUserPage(UserPageDTO userPageDTO) {

        return null;
    }
}
