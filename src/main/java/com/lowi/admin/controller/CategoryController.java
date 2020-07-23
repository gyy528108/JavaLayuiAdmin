package com.lowi.admin.controller;

import com.lowi.admin.entity.OneCategory;
import com.lowi.admin.entity.TwoCategory;
import com.lowi.admin.pojo.dto.CategoryDto;
import com.lowi.admin.service.CategoryService;
import com.lowi.admin.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * CategoryController.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/7/23 16:11
 */
@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/getOneCategory")
    public Result<List<OneCategory>> getOneCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.getOneCategory(categoryDto);
    }
    @RequestMapping("/getTwoCategory")
    public Result<List<TwoCategory>> getTwoCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.getTwoCategory(categoryDto);
    }
}
