package top.wytbook.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class CacheCountUtils {

    @Value("#{redisCountTemplate}")
    RedisTemplate<String, Long> redisTemplate;

    public boolean increment(String key, Long limitSecond, Long maxTime) {
        Long currentCount = redisTemplate.opsForValue().increment(key, 0L);;
        if (currentCount == null || currentCount.equals(0L)) {
            redisTemplate.opsForValue().increment(key);
            redisTemplate.expire(key, limitSecond, TimeUnit.SECONDS);
            return true;
        }
        else {
            if (currentCount < maxTime) {
                redisTemplate.opsForValue().increment(key);
                return true;
            }
            else {
                return false;
            }
        }
    }
    private static final String BAN_PREFIX = "ban:";

    public void banKey(String key, Long banTime) {
        redisTemplate.opsForValue().set(BAN_PREFIX + key, 1L, banTime, TimeUnit.SECONDS);
    }

    public boolean isBanKey(String key) {
        return redisTemplate.opsForValue().get(BAN_PREFIX + key) != null;
    }
}
