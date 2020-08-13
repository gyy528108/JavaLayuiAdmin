package com.lowi.admin.utils;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * RedisLockHelper.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/8/11 17:25
 */
@Service
public class RedisLockHelper {
    public static final String LOCK_PREFIX = "redis_lock";
    public static final int LOCK_EXPIRE = 1000; // ms

    private RedisTemplate redisTemplate;


    /**
     * Acquire a lock.
     *
     * @param key
     * @return got the lock or not
     */
    public boolean lock(String key) {
        String lock = LOCK_PREFIX + key;

        return (Boolean) redisTemplate.execute((RedisCallback) connection -> {
            long expireAt = System.currentTimeMillis() + LOCK_EXPIRE + 1;
            Boolean acquire = connection.setNX(lock.getBytes(), String.valueOf(expireAt).getBytes());
            if (acquire) {
                return true;
            } else {

                byte[] value = connection.get(lock.getBytes());

                if (Objects.nonNull(value) && value.length > 0) {

                    long expireTime = Long.parseLong(new String(value));

                    if (expireTime < System.currentTimeMillis()) {
                        // in case the lock is expired
                        byte[] oldValue = connection.getSet(lock.getBytes(), String.valueOf(System.currentTimeMillis() + LOCK_EXPIRE + 1).getBytes());
                        // avoid dead lock
                        return Long.parseLong(new String(oldValue)) < System.currentTimeMillis();
                    }
                }
            }
            return false;
        });
    }

    /**
     * Delete the lock
     *
     * @param key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

}
