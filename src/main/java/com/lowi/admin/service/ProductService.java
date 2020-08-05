package com.lowi.admin.service;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lowi.admin.dao.OneCategoryDao;
import com.lowi.admin.dao.ProductDao;
import com.lowi.admin.dao.TwoCategoryDao;
import com.lowi.admin.entity.*;
import com.lowi.admin.pojo.dto.ProductDto;
import com.lowi.admin.pojo.vo.ProductVo;
import com.lowi.admin.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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

    public Result getProductList(String token, Integer page, Integer limit, Integer oneCategoryId, String productInfo) {
        String userInfo = stringRedisTemplate.opsForValue().get(token);
        User user = JSONUtil.toBean(userInfo, User.class);
        Integer parentId;
        if (user.getParentId() == 0) {
            parentId = user.getId();
        } else {
            parentId = user.getParentId();
        }
        List<Product> records;
        int total;
        if (productInfo != null && !"".equals(productInfo)) {
            records = productDao.getProductList((page - 1) * limit, limit, parentId, oneCategoryId, productInfo);
            total = productDao.getProductTotal(parentId, oneCategoryId, productInfo);
        } else {
            QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parent_id", parentId);
            if (oneCategoryId != null) {
                queryWrapper.eq("one_category", oneCategoryId);
            }
            Page<Product> productPage = new Page<>(page, limit);
            IPage<Product> productIPage = productDao.selectPage(productPage, queryWrapper);
            records = productIPage.getRecords();
            total = (int) productIPage.getTotal();
        }
        List<ProductVo> productVos = new ArrayList<>();
        for (Product product : records) {
            ProductVo productVo = new ProductVo();
            BeanUtils.copyProperties(product, productVo);
            OneCategory oneCategory = oneCategoryDao.selectById(product.getOneCategory());
            productVo.setOneCategoryStr(oneCategory.getCategoryName());
            TwoCategory twoCategory = twoCategoryDao.selectById(product.getTwoCategory());
            productVo.setTwoCategoryStr(twoCategory.getCategoryName());
            productVos.add(productVo);
        }
        Result responseResult = new Result();
        responseResult.setCode(0);
        responseResult.setMsg("获取成功");
        responseResult.setData(productVos);
        responseResult.setCount(total);
        return responseResult;
    }

    public Result add(ProductDto productDto) {
        String userInfo = stringRedisTemplate.opsForValue().get(productDto.getToken());
        User user = JSONUtil.toBean(userInfo, User.class);
        Integer parentId;
        if (user.getParentId() == 0) {
            parentId = user.getId();
        } else {
            parentId = user.getParentId();
        }
        Product product = new Product();
        product.setCreateId(user.getId());
        product.setCreateName(user.getUsername());
        product.setCreateTime(new Date());
        product.setImg(productDto.getImgUrl());
        product.setOneCategory(productDto.getOneCategory());
        product.setTwoCategory(productDto.getTwoCategory());
        product.setParentId(parentId);
        product.setPrice(productDto.getPrice());
        product.setPriceUnit(productDto.getPriceUnit());
        product.setTotal(productDto.getCount());
        product.setUnit(productDto.getUnit());
        product.setProductName(productDto.getProductName());
        product.setProductContent(productDto.getContext());
        int insert = 0;
        try {
            insert = productDao.insert(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Result responseResult = new Result();
        if (insert == 0) {
            responseResult.setCode(1);
            responseResult.setMsg("系统异常，请重试");
            return responseResult;
        }
        responseResult.setCode(0);
        responseResult.setMsg("添加成功");
        return responseResult;

    }
}
