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

    @Transactional(rollbackFor = Exception.class)
    public Result editUserPage(UserPageDTO userPageDTO) {
        PageInit pageInit = pageInitDao.selectById(userPageDTO.getId());
        if (pageInit == null) {
            return Result.getInstance(0, "修改信息不存在");
        }
        //传递了父id并且 修改了页面的父id，则做处理
        if (userPageDTO.getParentId() != null && !userPageDTO.getParentId().equals(pageInit.getParentId())) {
            PageInit parentPageInit = pageInitDao.selectById(userPageDTO.getParentId());
            if (parentPageInit == null) {
                return Result.getInstance(0, "修改信息父页面不存在");
            }
        }
        PageInit page = new PageInit();
        page.setTitle(userPageDTO.getTitle());
        page.setIsSuperAdmin(userPageDTO.getIsSuperAdmin());
        page.setIsAdmin(userPageDTO.getIsAdmin());
        page.setParentId(userPageDTO.getParentId());
        page.setId(userPageDTO.getId());
        page.setIcon(userPageDTO.getIcon());
        int update = 0;
        try {
            update = pageInitDao.updateById(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.getInstance(update == 0 ? 1 : 0, update == 0 ? "系统异常，请稍后重试" : "更新成功");
    }
}
