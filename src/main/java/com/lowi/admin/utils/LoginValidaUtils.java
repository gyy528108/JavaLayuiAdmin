package com.lowi.admin.utils;

import cn.hutool.json.JSONUtil;
import com.lowi.admin.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * LoginValidaUtils.java
 * ==============================================
 * Copy right 2015-2017 by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @author : gengyy
 * @version : v2.0
 * @desc :
 * @since : 2020/7/1 11:04
 */
@Component
public class LoginValidaUtils {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static StringRedisTemplate redisTemplate;

    @PostConstruct
    public void beforeInit() {
        redisTemplate = stringRedisTemplate;
    }

    public static boolean loginValida(String token) {
        String tokenValida = redisTemplate.opsForValue().get(token);
        if (tokenValida == null) {
            return false;
        } else {
            User user = JSONUtil.toBean(tokenValida, User.class);
            String seconds = redisTemplate.opsForValue().get("userValida_" + user.getId());
            if (seconds == null) {
                return false;
            }
            String token1 = TokenUtils.createToken(user.getId(), seconds);
            if (!token.equals(token1)) {
                redisTemplate.delete(token);
                return false;
            }
            redisTemplate.expire(token, 30, TimeUnit.MINUTES);
        }
        return true;
    }

}
