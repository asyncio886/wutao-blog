package top.wytbook.util;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 验证码工具类
 */
@Component
public class CodeUtils {

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    private static final String CODE_PREFIX = "code:";

    private String getKey(String email) {
        return CODE_PREFIX + email;
    }

    public void setCodeToCache(String email, String code, int seconds) {
        stringRedisTemplate.opsForValue().set(getKey(email), code, seconds, TimeUnit.SECONDS);
    }

    public void removeCacheCode(String email) {
        stringRedisTemplate.opsForValue().decrement(getKey(email));
    }

    @Nullable
    public String getCacheCode(String email) {
        return stringRedisTemplate.opsForValue().get(getKey(email));
    }
}
