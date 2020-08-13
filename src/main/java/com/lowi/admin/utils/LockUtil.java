package com.lowi.admin.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * LockUtil.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/8/12 16:58
 */
@Component
public class LockUtil {
    static Logger logger = LoggerFactory.getLogger(LockUtil.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    private static StringRedisTemplate redisTemplate;

    @PostConstruct
    private void init() {
        redisTemplate = stringRedisTemplate;
    }

    public static boolean lock(String key, String value) {
        Boolean aBoolean = redisTemplate.opsForValue().setIfAbsent(key, value, 10, TimeUnit.SECONDS);
        if (aBoolean) {     //这个其实就是setnx命令，只不过在java这边稍有变化，返回的是boolea
            return true;
        }
        String currentValue = redisTemplate.opsForValue().get(key);
        //如果锁过期了
        if (!StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()) {
            //获取上一个锁的时间
            String oldValues = redisTemplate.opsForValue().getAndSet(key, value);
            /*
             * 只会让一个线程拿到锁
             * 如果旧的value和currentValue相等，只会有一个线程达成条件，因为第二个线程拿到的oldValue已经和currentValue不一样了
             */
            if (!StringUtils.isEmpty(oldValues) && oldValues.equals(currentValue)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 解锁
     *
     * @param key
     * @param value
     */
    public static void unlock(String key, String value) {
        try {
            String currentValue = redisTemplate.opsForValue().get(key);
            if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            logger.error("『redis分布式锁』解锁异常，{}", e);
        }
    }
}
