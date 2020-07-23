package com.lowi.admin.service;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.lowi.admin.dao.OneCategoryDao;
import com.lowi.admin.dao.TwoCategoryDao;
import com.lowi.admin.entity.OneCategory;
import com.lowi.admin.entity.Product;
import com.lowi.admin.entity.TwoCategory;
import com.lowi.admin.entity.User;
import com.lowi.admin.pojo.dto.CategoryDto;
import com.lowi.admin.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CategoryService.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/7/23 16:12
 */
@Service
public class CategoryService {
    @Autowired
    private OneCategoryDao oneCategoryDao;
    @Autowired
    private TwoCategoryDao twoCategoryDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public Result<List<OneCategory>> getOneCategory(CategoryDto categoryDto) {
        String userInfo = stringRedisTemplate.opsForValue().get(categoryDto.getToken());
        User user = JSONUtil.toBean(userInfo, User.class);
        QueryWrapper<OneCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", user.getParentId() == 0 ? user.getId() : user.getParentId());
        List<OneCategory> oneCategories = oneCategoryDao.selectList(queryWrapper);
        Result<List<OneCategory>> responseResult = new Result();
        responseResult.setCode(0);
        responseResult.setMsg("获取成功");
        responseResult.setData(oneCategories);
        responseResult.setCount(oneCategories.size());
        return responseResult;
    }

    public Result<List<TwoCategory>> getTwoCategory(CategoryDto categoryDto) {
        QueryWrapper<TwoCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("one_category_id", categoryDto.getParentId());
        List<TwoCategory> oneCategories = twoCategoryDao.selectList(queryWrapper);
        Result<List<TwoCategory>> responseResult = new Result();
        responseResult.setCode(0);
        responseResult.setMsg("获取成功");
        responseResult.setData(oneCategories);
        responseResult.setCount(oneCategories.size());
        return responseResult;
    }
}
