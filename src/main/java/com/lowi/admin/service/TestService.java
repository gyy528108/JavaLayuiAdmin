package com.lowi.admin.service;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lowi.admin.dao.UserDao;
import com.lowi.admin.entity.User;
import com.lowi.admin.utils.LockUtil;
import com.lowi.admin.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * TestService.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2019/12/18 16:03
 */
@Service
public class TestService {
    private static int MAP_COUNT = 30000;
    @Autowired
    private UserDao userMapper;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public User findUserByMobile(String phone) {
        User selectOne = new User();
        selectOne.setMobile(phone);
        User user = userMapper.selectOne(new QueryWrapper<>(selectOne));
        return user;
    }

    public User getUser() {
        User selectOne = new User();
        selectOne.setId(1);
        User user = userMapper.selectOne(new QueryWrapper<>(selectOne));
        return user;
    }

    @Async("asyncServiceExecutor")
    public void asyn(Integer productId) {
        Long increment = stringRedisTemplate.opsForValue().increment("product_" + productId, -1);
        if (increment < 0) {
            logger.info("草泥马这么快就没了");
            stringRedisTemplate.opsForValue().increment("product_" + productId, 1);
        } else {
            logger.info("购买成功:" + stringRedisTemplate.opsForValue().get("product_" + productId));
        }
    }

    @Async("asyncServiceExecutor")
    public Result lock(String key, String value, int i) {
        Result result = new Result();
        boolean lock = LockUtil.lock(key, value);
        if (!lock) {
            result.setCode(1);
            result.setMsg("对不起没抢到锁");
            result.setData(i);
            System.out.println("result = " + result);
            return result;
        }
        try {
            Boolean phone = stringRedisTemplate.opsForSet().isMember("phone", key);
            if (phone) {
                result.setCode(1);
                result.setMsg("该手机号已存在");
                result.setData(i);
                System.out.println("result = " + result);
                return result;
            }
            System.out.println("我抢到了 = " + key);
            TimeUnit.SECONDS.sleep(2);
            stringRedisTemplate.opsForSet().add("phone", key);
        } catch (Exception e) {
            result.setCode(1);
            result.setMsg("系统异常，请重试");
            result.setData(i);
            System.out.println("result = " + result);
            return result;
        } finally {
            LockUtil.unlock(key, value);
        }
        result.setCode(0);
        result.setMsg("你抢到了");
        result.setData(i);
        System.out.println("result = " + result);
        return result;
    }

    @Async("asyncServiceExecutor")
    public void asyn1(Map<String, Object> map) {
        map.put("王睿", RandomUtil.randomInt(1, 50));
        logger.info("线程2修改{}", map.get("王睿"));
    }


    private static void mapCapacity() {
        List<Integer> list = new ArrayList<>(MAP_COUNT);
        for (int i = 0; i < MAP_COUNT; i++) {
            list.add(i);
        }
        long start = System.currentTimeMillis();
        HashMap<Integer, Integer> map = new HashMap<>((int) (MAP_COUNT / 0.75) + 1);
        for (int i = 0; i < list.size(); i++) {
            map.put(list.get(i), list.get(i));
        }
        long end = System.currentTimeMillis();
        System.out.println("end = " + (end - start));
    }

    private static void mapCapacity2() {
        List<Integer> list = new ArrayList<>(MAP_COUNT);
        for (int i = 0; i < MAP_COUNT; i++) {
            list.add(i);
        }
        long start = System.currentTimeMillis();
        Map<Integer, Integer> map = new HashMap((int) (MAP_COUNT / 0.75) + 1);
        for (int i = 0; i < list.size(); i++) {
            map.put(list.get(i), list.get(i));
        }
        long end = System.currentTimeMillis();

        System.out.println("end2 = " + (end - start));
    }

}
