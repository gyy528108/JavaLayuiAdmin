package com.lowi.admin.controller;

import com.lowi.admin.service.ProductService;
import com.lowi.admin.utils.Result;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * ProductController.java
 * ==============================================
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/7/22 21:06
 */
@RequestMapping("product")
@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @RequestMapping("/getProductList")
    public Result getProductList(String token, Integer page, Integer limit) {

        return productService.getProductList(token, page, limit);
    }

    @RequestMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile multipartFile) {
        System.out.println("multipartFile = " + multipartFile);
        return null;
    }


}
