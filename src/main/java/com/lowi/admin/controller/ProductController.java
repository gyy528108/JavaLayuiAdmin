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
    @Autowired
    private ProductService productService;

    @RequestMapping("/getProductList")
    public Result getProductList(String token, Integer page, Integer limit) {

        return productService.getProductList(token, page, limit);
    }

    @RequestMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile multipartFile) throws Exception {
        System.out.println("multipartFile = " + multipartFile);
        String s = productionFile(multipartFile);
        Map<String, Object> map = new HashMap<>();
        map.put("img", s);
        Result responseResult = new Result();
        responseResult.setCode(0);
        responseResult.setMsg("上传成功");
        responseResult.setData(map);
        return responseResult;
    }

    @RequestMapping("/add")
    public Result add(@RequestBody ProductDto productDto)  {
        return productService.add(productDto);
    }

    private String productionFile(MultipartFile multipartFile) {
        String name = multipartFile.getOriginalFilename();
        int lastIndexOf = name.lastIndexOf(".");
        String endStr = name.substring(lastIndexOf);
        String timePath = LocalDate.now().toString();
        timePath = timePath.replaceAll("-", "");
        String baseUrl = timePath + "/" + DateUtil.currentSeconds() + endStr;
        String activeProfile = ProfileConfig.getActiveProfile();
        String fileLocal;
        if (activeProfile.equals("dev")) {
            fileLocal = "http://10.10.10.22/img/";
        } else {
            fileLocal = uploadUrl;
        }
        try {
            downloadFile(multipartFile, uploadUrl + baseUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileLocal + baseUrl;
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

}
