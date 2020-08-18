package com.lowi.admin.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.lowi.admin.entity.PageIcon;
import com.lowi.admin.entity.User;
import com.lowi.admin.pojo.dto.ServiceResponse;
import com.lowi.admin.pojo.dto.UserDto;
import com.lowi.admin.pojo.vo.FeishuAccountVo;
import com.lowi.admin.pojo.vo.ProductVo;
import com.lowi.admin.pojo.vo.TreadDemo;
import com.lowi.admin.service.PageIconService;
import com.lowi.admin.service.TestService;
import com.lowi.admin.utils.LockUtil;
import com.lowi.admin.utils.Result;
import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import com.sun.corba.se.impl.orbutil.concurrent.SyncUtil;
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
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

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
    @Autowired
    TestService testService;
    @Autowired
    PageIconService pageIconService;


    private Logger logger = LoggerFactory.getLogger(TestController.class);

    @RequestMapping("/test")
    public Result test() {
//        long l = System.currentTimeMillis() + 10 * 1000;
//        for (int i = 0; i < 5; i++) {
//            testService.lock("13462184511", String.valueOf(l), i);
//        }
        insertIcon("E:/icon.txt");
        Result result = new Result();
        result.setCode(0);
        return result;
    }

    @RequestMapping("/test1")
    public Result<Map<String, Object>> test1(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        Map<String, Object> feishuToken = getFeishuToken(4105);
        ContentType strContent = ContentType.create("text/plain", Charset.forName("UTF-8"));
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://open.feishu.cn/open-apis/image/v4/put/");
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

    public void insertIcon(String url) {
        File file = new File(url);
        String rgex = "\\.(.*?):before";
        Pattern pattern = Pattern.compile(rgex);

        try {
            //构造一个BufferedReader类来读取文件
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            List<PageIcon> pageIcons = new ArrayList<>();
            //使用readLine方法，一次读一行
            while ((s = br.readLine()) != null) {
                Matcher m = pattern.matcher(s);
                while (m.find()) {
                    String group = m.group(1);
                    System.out.println("group = " + group);
                    PageIcon pageIcon = new PageIcon();
                    pageIcon.setIcon("fa "+group);
                    pageIcon.setCreateTime(new Date());
                    pageIcons.add(pageIcon);
                }
            }
            pageIconService.batchInsert(pageIcons);

        } catch (Exception e) {
            e.printStackTrace();
        }
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
                if (serviceResponse != null && "0".equals(serviceResponse.getMeta().getStatus())) {
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

    public static List<FeishuAccountVo> getFeishuMiniProgramUser(Integer companyId, String[] userIds) {
        Map<String, Object> map = new HashMap<>(16);
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
                if (serviceResponse != null && "000000".equals(serviceResponse.getMeta().getStatus())) {
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


    private static void subListDemo() {
        List<String> stringList = new ArrayList() {{
            add("H");
            add("O");
            add("L");
            add("L");
            add("E");
            add("I");
            add("S");
        }};
        List<String> list = stringList.subList(2, 5);

        System.out.println("list = " + list);

        list.set(0, "666");

        System.out.println("list = " + list);

        System.out.println("stringList = " + stringList);
    }

    class LRUCache<K, V> extends LinkedHashMap<K, V> {
        private final int CACHE_SIZE;

        LRUCache(int cacheSize) {
            super((int) Math.ceil(cacheSize / 0.75) + 1, .75f, true);
            CACHE_SIZE = cacheSize;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > CACHE_SIZE;
        }
    }

    private volatile static long COUNT = 1L;
    private static AtomicInteger atomicInteger = new AtomicInteger(10);//CAS操作即比较并替换，实现并发算法时常用到的一种技术 不能避免ABA问题
    static AtomicStampedReference<Integer> stampedReference = new AtomicStampedReference<Integer>(10, 1);

    public static void main(String[] args) {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 100; i++) {
            int randomInt = RandomUtil.randomInt(1, 10);
            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    if (atomicInteger.get() - randomInt < 0) {
                        System.out.println("数量不够");
                    } else {
                        atomicInteger.set(atomicInteger.get() - randomInt);
                        System.out.println("randomInt = " + randomInt);
                        System.out.println("get = " + atomicInteger.get());
                    }
                }
            });
        }
    }
}
