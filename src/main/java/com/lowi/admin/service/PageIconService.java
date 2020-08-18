package com.lowi.admin.service;

import com.lowi.admin.dao.PageIconDao;
import com.lowi.admin.dao.PageInitDao;
import com.lowi.admin.entity.PageIcon;
import com.lowi.admin.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * PageIconService.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/8/17 16:39
 */
@Service
public class PageIconService {
    @Autowired
    PageIconDao pageIconDao;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    final String ICON_KEY = "page_icon";

    @Transactional(rollbackFor = Exception.class)
    public void batchInsert(List<PageIcon> pageIconList) {
        pageIconDao.batchInsert(pageIconList);
    }

    public Result getAllIcon(String token) {
        List<String> range = stringRedisTemplate.opsForList().range(ICON_KEY, 0, -1);
        if (range != null && range.size() > 0) {
            return Result.getInstance(0, "获取成功", range.size(), range);
        }
        List<PageIcon> pageIcons = pageIconDao.selectList(null);
        for (PageIcon pageIcon : pageIcons) {
            stringRedisTemplate.opsForList().leftPush(ICON_KEY, pageIcon.getIcon());
        }

        List<String> iconList = stringRedisTemplate.opsForList().range(ICON_KEY, 0, -1);
        return Result.getInstance(0, "获取成功", pageIcons.size(), iconList);
    }
}
