package com.lowi.admin.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lowi.admin.pojo.dto.ServiceResponse;
import com.lowi.admin.pojo.vo.FeishuAccountVo;
import com.lowi.admin.utils.Result;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * TestController.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/7/24 10:23
 */
@RequestMapping("test")
@RestController
public class TestController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    private Logger logger = LoggerFactory.getLogger(TestController.class);

    @RequestMapping("/test")
    public Result<Map<String, Object>> test() {
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/test1")
    public Result<Map<String, Object>> test1(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        Map<String, Object> feishuToken = getFeishuToken(4105);
        ContentType strContent = ContentType.create("text/plain", Charset.forName("UTF-8"));
        CloseableHttpClient httpclient = HttpClients.createDefault();


        HttpPost httpPost = new HttpPost("https://open.feishu.cn/open-apis/image/v4/put/");
//        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Authorization", "Bearer " + (String) feishuToken.get("access_token"));

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        builder.addBinaryBody("image", multipartFile.getInputStream(), ContentType.DEFAULT_BINARY, multipartFile.getOriginalFilename());
        builder.addTextBody("image_type", "message", strContent);
        //Step4：转化为消息体
        HttpEntity entity = builder.build();
        httpPost.setEntity(entity);
        CloseableHttpResponse execute = httpclient.execute(httpPost);
        HttpEntity resEntity = execute.getEntity();
        String resContent = EntityUtils.toString(resEntity);
        System.out.println("resContent = " + resContent);
        return null;
    }


    public static Map<String, Object> getFeishuToken(Integer companyId) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            ContentType strContent = ContentType.create("text/plain", StandardCharsets.UTF_8);

            HttpPost httpPost = new HttpPost("https://dev-xsapi.51lick.cn/miniapppublic/getFeiShuTenantAccessToken");
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            entityBuilder.addTextBody("company_id", String.valueOf(companyId), strContent);
            entityBuilder.addTextBody("app_key", "wisdom", strContent);
            HttpEntity entity = entityBuilder.build();

            httpPost.setEntity(entity);

            response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null && response.getStatusLine().getStatusCode() == 200) {
                String resContent = EntityUtils.toString(resEntity);
                System.out.println("doPost response content:" + resContent);
                ServiceResponse serviceResponse = JSONUtil.toBean(resContent, ServiceResponse.class);
                if (serviceResponse != null && serviceResponse.getMeta().getStatus().equals("0")) {
                    Object items = serviceResponse.getData().getItems();
                    if (items == null) {
                        return null;
                    }
                    return (Map<String, Object>) items;
                } else {
                    System.out.println("getFeishuToken接口请求报错 ：" + resContent);
                }
            }
        } catch (Exception e) {
            System.out.println("getFeishuToken接口报错 ：" + e);
            return null;
        }
        return null;
    }


    private static void feishuImgSend(List<FeishuAccountVo> listList, String token) {
        String imgKay = "img_571d16bd-5e35-4fa6-82f1-5f54a4dd67ag";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        HttpPost httpPost = new HttpPost("https://open.feishu.cn/open-apis/message/v4/send/");
        try {
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Authorization", "Bearer " + token);
            JSONObject obj = JSONUtil.createObj();
            for (int i = 0; i < listList.size(); i++) {
                obj.put("open_id", listList.get(i).getFeiShuMiniProgramOpenId());
                obj.put("msg_type", "image");
                JSONObject content = JSONUtil.createObj();
                content.put("image_key", imgKay);
                obj.put("content", content);
                StringEntity stringEntity = new StringEntity(JSONUtil.toJsonStr(obj), Consts.UTF_8);
                stringEntity.setContentType("application/json; charset=UTF-8");
                httpPost.setEntity(stringEntity);
                response = httpClient.execute(httpPost);
                HttpEntity resEntity = response.getEntity();
                String resContent = EntityUtils.toString(resEntity);
                System.out.println("resContent------------ = " + resContent);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Integer[] int2 = new Integer[]{4105,1935};
        for (int i = 0; i < int2.length; i++) {
            Map<String, Object> feishuToken = getFeishuToken(int2[i]);
            List<FeishuAccountVo> feishuMiniProgramUser = getFeishuMiniProgramUser(int2[i], null);
            feishuImgSend(feishuMiniProgramUser, (String) feishuToken.get("access_token"));
        }
    }

    public static List<FeishuAccountVo> getFeishuMiniProgramUser(Integer companyId, String[] userIds) {
        Map<String, Object> map = new HashMap<>();
        map.put("companyId", companyId);
        map.put("userIdList", userIds);
        String content = JSONUtil.toJsonStr(map);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost("http://10.10.10.174:8800/feiShuMiniProgramUser/getByCondition");
            StringEntity stringEntity = new StringEntity(content, Consts.UTF_8);
            stringEntity.setContentType("application/json; charset=UTF-8");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null && response.getStatusLine().getStatusCode() == 200) {
                String resContent = EntityUtils.toString(resEntity);
                System.out.println("doPost response content:" + resContent);
                ServiceResponse serviceResponse = JSONUtil.toBean(resContent, ServiceResponse.class);
                if (serviceResponse != null && serviceResponse.getMeta().getStatus().equals("000000")) {
                    Object items = serviceResponse.getData().getItems();
                    if (items == null) {
                        return null;
                    }
                    return JSONUtil.toList(JSONUtil.parseArray(items), FeishuAccountVo.class);
                } else {
                    System.out.println("getWeLinkMiniProgramUser接口请求报错 ：" + resContent);
                }
            }
        } catch (Exception e) {
            System.out.println("getWeLinkMiniProgramUser接口报错 ：" + e);
            return null;
        }
        return null;
    }
}
