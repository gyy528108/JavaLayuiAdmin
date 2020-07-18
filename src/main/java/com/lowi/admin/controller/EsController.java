//package com.lowi.admin.controller;
//
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.core.map.MapUtil;
//import cn.hutool.core.util.RandomUtil;
//import cn.hutool.json.JSONUtil;
//import com.lowi.admin.entity.AgencyInfo;
//import org.apache.commons.lang3.time.DateUtils;
//import com.lowi.admin.enums.AgencyEsSortEnum;
//import org.elasticsearch.action.search.SearchRequest;
//import org.elasticsearch.action.search.SearchResponse;
//import org.elasticsearch.client.RequestOptions;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.script.Script;
//import org.elasticsearch.search.SearchHit;
//import org.elasticsearch.search.builder.SearchSourceBuilder;
//import org.elasticsearch.search.sort.ScriptSortBuilder;
//import org.elasticsearch.search.sort.SortBuilders;
//import org.elasticsearch.search.sort.SortOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.Cursor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ScanOptions;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.thymeleaf.util.MapUtils;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * EsController.java
// * ==============================================
// * Copy right 2015-2017 by http://www.51lick.com
// * ----------------------------------------------
// * This is not a free software, without any authorization is not allowed to use and spread.
// * ==============================================
// *
// * @author : gengyy
// * @version : v2.0
// * @desc :
// * @since : 2020/7/13 10:27
// */
//@RestController
//@RequestMapping("es")
//public class EsController {
//    @Autowired
//    private RestHighLevelClient client;
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//
//    @RequestMapping("/test")
//    public Map<String, Object> es(Integer page, Integer count) {
//        if (page == null) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("data", "案家属将此事件");
//            return map;
//        }
//        if (page > 1) {
//            Map<String, Object> map = new HashMap<>();
//            Long size = redisTemplate.opsForList().size("agencyEsList");
//            if (size != null && size > 0) {
//                List<Object> agencyEs = redisTemplate.opsForList().range("agencyEsList", (page - 1) * count, (page * count) - 1);
//                map.put("total", size);
//                map.put("data", agencyEs);
//                System.out.println("map = " + map);
//                return map;
//            }
//        }
//
//        //构建搜索条件
//        SearchSourceBuilder builder = new SearchSourceBuilder();
//        //最外层语句构建器
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        String sort = null;
//        int randomInt = RandomUtil.randomInt(1, 2);
//        if (randomInt == 1) {
//            sort = "ASC";
//        }
//        if (randomInt == 2) {
//            sort = "DESC";
//        }
//
//        Long synAgencyEs = stringRedisTemplate.opsForSet().size("synAgencyEs");
//        int synTotal = 0;
//        assert synAgencyEs != null;
//        synTotal = synAgencyEs.intValue();
//        if (synAgencyEs == 1000) {
//            Map<String, Object> map = new HashMap<>();
//            Long size = redisTemplate.opsForList().size("agencyEsList");
//            if (size != null && size > 0) {
//                List<Object> agencyEs = redisTemplate.opsForList().range("agencyEsList", (page - 1) * count, (page * count) - 1);
//                map.put("total", size);
//                map.put("data", agencyEs);
//                System.out.println("map = " + map);
//                return map;
//            }
//            return null;
//        }
//
//        stringRedisTemplate.delete("agencyEs");
//        redisTemplate.delete("agencyEsList");
//        StringBuilder ids = new StringBuilder();
//        Cursor<String> cursor = stringRedisTemplate.opsForSet().scan("synAgencyEs", ScanOptions.NONE);
//        while (cursor.hasNext()) {
//            Object object = cursor.next();
//            ids.append(object).append(",");
//            boolQueryBuilder.mustNot(QueryBuilders.termQuery("id", object));
//        }
//        Script script = new Script("Math.random()");
//        ScriptSortBuilder scriptSortBuilder = SortBuilders.scriptSort(script, ScriptSortBuilder.ScriptSortType.NUMBER).order(SortOrder.valueOf(sort));
//        boolQueryBuilder.must(QueryBuilders.termQuery("is_del", 1));
//        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("province_name", "河南省"));
//        boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("city_name", "安阳市"));
//        boolQueryBuilder.must(QueryBuilders.rangeQuery("company_establish_date").gte("2017-07-15").lt("2019-07-15"));
//        builder.query(boolQueryBuilder);
//        builder.sort(scriptSortBuilder);
//        builder.size(1000 - synTotal);
//        SearchRequest request = new SearchRequest();
//        request.indices("lyx_agency_info");
//        request.source(builder);
//        System.out.println("进入es查询 = " + DateUtil.now());
//        List<Map<String, Object>> backList = new ArrayList<>();
//        try {
//            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
//            SearchHit[] hits = response.getHits().getHits();
//            System.out.println("es查询完毕 = " + DateUtil.now());
//            System.out.println("redis处理开始 = " + DateUtil.now());
//            for (SearchHit hit : hits) {
//                Map<String, Object> resumeData = hit.getSourceAsMap();
//                Object id = resumeData.get("id");
//                String esId = id == null ? null : String.valueOf(id);
//                Boolean agencyEs = stringRedisTemplate.opsForSet().isMember("synAgencyEs", esId);
//                if (agencyEs != null && !agencyEs) {
//                    backList.add(resumeData);
//                    if ((backList.size() + synAgencyEs) == 1000) {
//                        System.out.println("backList.size() = " + backList.size());
//                        break;
//                    }
//                }
//            }
//            System.out.println("es查询完毕redis处理完毕 = " + DateUtil.now());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("redis 查询values完毕 = " + DateUtil.now());
//        List<AgencyInfo> mapList = new ArrayList<>();
//        for (int i = 0; i < backList.size(); i++) {
//            String str = JSONUtil.toJsonStr(backList.get(i));
//            mapList.add(JSONUtil.toBean(str, AgencyInfo.class));
//        }
//        redisTemplate.opsForList().leftPushAll("agencyEsList", mapList);
//        System.out.println("redis处理成类完毕 = " + DateUtil.now());
//        Map<String, Object> map = new HashMap<>();
//        Long size = redisTemplate.opsForList().size("agencyEsList");
//        if (size != null && size > 0) {
//            List<Object> agencyEs = redisTemplate.opsForList().range("agencyEsList", (page - 1) * count, (page * count) - 1);
//            map.put("total", size);
//            map.put("data", agencyEs);
//            System.out.println("map = " + map);
//            return map;
//        }
//        return map;
//    }
//
//    @RequestMapping("/test1")
//    private Map<String, Object> getPhoneRecordRedis(Integer page, Integer count) {
//        StringBuilder str = new StringBuilder();
//        Cursor<String> cursor = stringRedisTemplate.opsForSet().scan("synAgencyEs", ScanOptions.NONE);
//        while (cursor.hasNext()) {
//            Object object = cursor.next();
//            str.append(object).append(",");
//            System.out.println("object = " + object);
//        }
//        System.out.println("str = " + str);
//        return null;
//    }
//
//    @RequestMapping("/agencyEs")
//    private Map<String, Object> agencyEs(Integer esId) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("esId", esId);
//        stringRedisTemplate.opsForHash().put("synAgencyEs", String.valueOf(esId), JSONUtil.toJsonStr(map));
//        stringRedisTemplate.opsForHash().delete("agencyEs", String.valueOf(esId));
//
//
//        redisTemplate.delete("agencyEsList");
//        List<Object> values = stringRedisTemplate.opsForHash().values("agencyEs");
//        List<AgencyInfo> mapList = new ArrayList<>();
//        for (int i = 0; i < values.size(); i++) {
//            String str = JSONUtil.toJsonStr(values.get(i));
//            mapList.add(JSONUtil.toBean(str, AgencyInfo.class));
//        }
//        redisTemplate.opsForList().leftPushAll("agencyEsList", mapList);
//        return map;
//    }
//}
