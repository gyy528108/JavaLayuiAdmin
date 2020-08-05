package com.lowi.admin.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.lowi.admin.entity.AgencyInfo;
import com.lowi.admin.pojo.dto.SearchPortraitParam;
import com.lowi.admin.utils.EsUtils;
import com.lowi.admin.utils.Result;
import org.apache.commons.lang3.time.DateUtils;
import com.lowi.admin.enums.AgencyEsSortEnum;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.ScriptSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * EsController.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/7/13 10:27
 */
@RestController
@RequestMapping("es")
public class EsController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    private static String synMiniProgramAgency = "synMiniProgramAgencyEs_";
    private static String miniProgramAgencyEsList = "miniProgramAgencyEsList_";


    @RequestMapping("/test")
    public Result es() {
        SearchPortraitParam unicomTaskParam = new SearchPortraitParam();
        unicomTaskParam.setCompanyId(RandomUtil.randomInt());
        unicomTaskParam.setUserId(RandomUtil.randomInt());
        Result result=new Result();
        Integer page = unicomTaskParam.getPage();
        Integer pageSize = unicomTaskParam.getPageSize();
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 20;
        }
        //判断是否传递排序
        String sort = "DESC";
        if (unicomTaskParam.getIsSort() == null) {
            unicomTaskParam.setIsSort(0);
            sort = "DESC";
        }
        if (unicomTaskParam.getMiniProgramSort() != null) {
            sort = unicomTaskParam.getMiniProgramSort().toUpperCase();
        }
        //点击了排序 则清理redis集合 重新排序不走随机
        if (unicomTaskParam.getIsSort() == 1 && page == 1) {
            Long size = redisTemplate.opsForList().size(miniProgramAgencyEsList + unicomTaskParam.getCompanyId() + "_" + unicomTaskParam.getUserId());
            //redis没数据，说明es没数据
            if (size > 0) {
                //获取redis列表数据
                List<Map<String, Object>> agencyEs = redisTemplate.opsForList().range(miniProgramAgencyEsList + unicomTaskParam.getCompanyId() + "_" + unicomTaskParam.getUserId(), 0, -1);
                //把原来的redis列表数据删除
                redisTemplate.delete(miniProgramAgencyEsList + unicomTaskParam.getCompanyId() + "_" + unicomTaskParam.getUserId());

                if (agencyEs.size() > 0) {
                    //重新排序被剔除后redis的数据
                    listSort(agencyEs, sort);
                    redisTemplate.opsForList().leftPushAll(miniProgramAgencyEsList + unicomTaskParam.getCompanyId() + "_" + unicomTaskParam.getUserId(), agencyEs);
                }
                Long total = redisTemplate.opsForList().size(miniProgramAgencyEsList + unicomTaskParam.getCompanyId() + "_" + unicomTaskParam.getUserId());
                if (total > 0) {
                    List<Object> newRedisData = redisTemplate.opsForList().range(miniProgramAgencyEsList + unicomTaskParam.getCompanyId() + "_" + unicomTaskParam.getUserId(), (page - 1) * pageSize, (page * pageSize) - 1);
                    result.setData(newRedisData);
                    result.setCode(0);
                    return result;
                }
                result.setCode(1);
                return result;
            }
            result.setCode(1);
            return result;
        }
        //如果page > 1 则走redis经销商数据列表分页
        if (page > 1) {
            Long size = redisTemplate.opsForList().size(miniProgramAgencyEsList + unicomTaskParam.getCompanyId() + "_" + unicomTaskParam.getUserId());
            if (size != null && size > 0) {

                List<Object> agencyEs = redisTemplate.opsForList().range(miniProgramAgencyEsList + unicomTaskParam.getCompanyId() + "_" + unicomTaskParam.getUserId(), (page - 1) * pageSize, (page * pageSize) - 1);
                result.setData(agencyEs);
                result.setCode(0);
                return result;
            }
        }
        //如果page == 1 则走随机查询
        if (page == 1 && unicomTaskParam.getIsSort() == 0) {
            //删除掉存入到redis的经销商数据列表
            redisTemplate.delete(miniProgramAgencyEsList + unicomTaskParam.getCompanyId() + "_" + unicomTaskParam.getUserId());
        }
        Cursor<String> cursor = stringRedisTemplate.opsForSet().scan(synMiniProgramAgency + unicomTaskParam.getCompanyId() + "_" + unicomTaskParam.getUserId(), ScanOptions.NONE);
        while (cursor.hasNext()) {
            Object object = cursor.next();
            System.out.println("object = " + object);
        }
        List<Map<String, Object>> backList = new ArrayList<>();

        try {
            Thread.sleep(5000);
            System.out.println("模拟获取es数据2秒钟");
            for (int i = 0; i < 500; i++) {
                Map<String, Object> map=new HashMap<>();
                map.put(RandomUtil.randomNumbers(3),RandomUtil.randomNumber());
                backList.add(map);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if (backList.size() > 0) {
            //从es查询出来的加入redis这部分属于未同步
            redisTemplate.opsForList().leftPushAll(miniProgramAgencyEsList + unicomTaskParam.getCompanyId() + "_" + unicomTaskParam.getUserId(), backList);
            redisTemplate.expire(miniProgramAgencyEsList + unicomTaskParam.getCompanyId() + "_" + unicomTaskParam.getUserId(), 4, TimeUnit.HOURS);
        }
        //从redis取出数据
        Long size = redisTemplate.opsForList().size(miniProgramAgencyEsList + unicomTaskParam.getCompanyId() + "_" + unicomTaskParam.getUserId());
        if (size > 0) {
            List<Object> agencyEs = redisTemplate.opsForList().range(miniProgramAgencyEsList + unicomTaskParam.getCompanyId() + "_" + unicomTaskParam.getUserId(), (page - 1) * pageSize, (page * pageSize) - 1);
            result.setData(agencyEs);
            result.setCode(0);
            return result;
        }
        result.setCode(1);
        return result;
    }

    @RequestMapping("/test1")
    private Map<String, Object> getPhoneRecordRedis(Integer page, Integer count) {
        StringBuilder str = new StringBuilder();
        Cursor<String> cursor = stringRedisTemplate.opsForSet().scan("synAgencyEs", ScanOptions.NONE);
        while (cursor.hasNext()) {
            Object object = cursor.next();
            str.append(object).append(",");
            System.out.println("object = " + object);
        }
        System.out.println("str = " + str);
        return null;
    }
    private void listSort(List<Map<String, Object>> agencyEs, String sort) {
        Collections.sort(agencyEs, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                long o1CreateTime = (long) o1.get("create_time");
                long o2CreateTime = (long) o2.get("create_time");
                long l = 0;
                if ("desc".toLowerCase().equals(sort)) {
                    l = o1CreateTime - o2CreateTime;
                }
                if ("asc".toLowerCase().equals(sort)) {
                    l = o2CreateTime - o1CreateTime;
                }
                return (int) l;
            }
        });
    }
    @RequestMapping("/agencyEs")
    private Map<String, Object> agencyEs(Integer esId) {


        Map<String, Object> map = new HashMap<>();
        map.put("esId", esId);
        stringRedisTemplate.opsForHash().put("synAgencyEs", String.valueOf(esId), JSONUtil.toJsonStr(map));
        stringRedisTemplate.opsForHash().delete("agencyEs", String.valueOf(esId));


        redisTemplate.delete("agencyEsList");
        List<Object> values = stringRedisTemplate.opsForHash().values("agencyEs");
        List<AgencyInfo> mapList = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            String str = JSONUtil.toJsonStr(values.get(i));
            mapList.add(JSONUtil.toBean(str, AgencyInfo.class));
        }
        redisTemplate.opsForList().leftPushAll("agencyEsList", mapList);
        return map;
    }

    @RequestMapping("/test2")
    private void test2() {
        RestHighLevelClient client = EsUtils.getClient();
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.rangeQuery("tj_adt").gte("2020-04-02 00:00:00").lt("2020-04-02 23:59:59"));
        SearchRequest request = new SearchRequest();
        request.indices("collectionlog_access_xbcloud_prod");
        request.source(builder);
        List<Map<String, Object>> backList = new ArrayList<>();
        try {
            SearchResponse response =  client.search(request, RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();
            for (SearchHit hit : hits) {
                Map<String, Object> resumeData = hit.getSourceAsMap();
                backList.add(resumeData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("backList = " + backList);
    }
}
