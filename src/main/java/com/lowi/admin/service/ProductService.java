package com.lowi.admin.service;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lowi.admin.dao.OneCategoryDao;
import com.lowi.admin.dao.ProductDao;
import com.lowi.admin.dao.TwoCategoryDao;
import com.lowi.admin.entity.*;
import com.lowi.admin.pojo.vo.ProductVo;
import com.lowi.admin.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * ProductService.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/7/22 21:06
 */
@Service
public class ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private OneCategoryDao oneCategoryDao;
    @Autowired
    private TwoCategoryDao twoCategoryDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public Result getProductList(String token, Integer page, Integer limit) {
        String userInfo = stringRedisTemplate.opsForValue().get(token);
        User user = JSONUtil.toBean(userInfo, User.class);
        Integer parentId;
        if (user.getParentId() == 0) {
            parentId = user.getId();
        } else {
            parentId = user.getParentId();
        }
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentId);
        Page<Product> productPage = new Page<>(page, limit);
        IPage<Product> productIPage = productDao.selectPage(productPage, queryWrapper);
        List<Product> records = productIPage.getRecords();
        List<ProductVo> productVos = new ArrayList<>();
        for (Product product : records) {
            ProductVo productVo = new ProductVo();
            BeanUtils.copyProperties(product, productVo);
            TwoCategory twoCategory = twoCategoryDao.selectById(product.getTwoCategory());
            productVo.setTwoCategoryStr(twoCategory.getCategoryName());
            productVos.add(productVo);
        }
        Result responseResult = new Result();
        responseResult.setCode(0);
        responseResult.setMsg("获取成功");
        responseResult.setData(productVos);
        responseResult.setCount((int) productIPage.getTotal());
        return responseResult;
    }
}
