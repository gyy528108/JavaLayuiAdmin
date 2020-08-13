package com.lowi.admin.controller;

import cn.hutool.core.date.DateUtil;
import com.lowi.admin.config.ProfileConfig;
import com.lowi.admin.pojo.dto.ProductDto;
import com.lowi.admin.service.ProductService;
import com.lowi.admin.utils.Result;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    @Value("${uploadUrl}")
    private String uploadUrl;
    @Value("${fileUrl}")
    private String fileUrl;
    @Autowired
    private ProductService productService;
    private static String DEV = "dev";

    @RequestMapping("/getProductList")
    public Result getProductList(String token, Integer page, Integer limit, Integer oneCategory, String productInfo) {
        System.out.println("productInfo = " + productInfo);
        return productService.getProductList(token, page, limit, oneCategory, productInfo);
    }

    @RequestMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile multipartFile) throws Exception {

        System.out.println("multipartFile = " + multipartFile);
        String s = productionFile(multipartFile);
        Map<String, Object> map = new HashMap<>(16);
        map.put("img", s);
        Result responseResult = new Result();
        responseResult.setCode(0);
        responseResult.setMsg("上传成功");
        responseResult.setData(map);
        return responseResult;
    }

    @RequestMapping("/add")
    public Result add(@RequestBody ProductDto productDto) {
        return productService.add(productDto);
    }

    private String productionFile(MultipartFile multipartFile) {
        String name = multipartFile.getOriginalFilename();
        int lastIndexOf = name.lastIndexOf(".");
        String endStr = name.substring(lastIndexOf);
        String timePath = LocalDate.now().toString();
        timePath = timePath.replaceAll("-", "");
        String baseUrl = timePath + "/" + DateUtil.currentSeconds() + endStr;
        try {
            downloadFile(multipartFile, uploadUrl + baseUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl + baseUrl;
    }

    public void downloadFile(MultipartFile multipartFile, String fileLocal) throws Exception {

        DataInputStream in = new DataInputStream(multipartFile.getInputStream());
        createFile(fileLocal);
        DataOutputStream out = new DataOutputStream(new FileOutputStream(fileLocal));
        byte[] buffer = new byte[2048];
        int count = 0;
        while ((count = in.read(buffer)) > 0) {
            out.write(buffer, 0, count);
        }
        out.close();
        in.close();
    }

    private File createFile(String path) throws IOException {
        File file = new File(path);
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }
        file.createNewFile();
        return file;
    }

    public static void main(String[] args) {
        String a = "null2";
        String toString = Objects.toString(a, null);
        System.out.println("toString = " + toString);


        String str = "a,b,c,,";
        String[] ary = str.split(",", -1);
        for (int i = 0; i < ary.length; i++) {
            System.out.println("ary = " + ary[i]);
        }
// 预期大于 3，结果是 3
        System.out.println(ary.length);

    }

}
