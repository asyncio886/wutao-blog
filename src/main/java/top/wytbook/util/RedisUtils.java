package top.wytbook.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RedisUtils {

    @Value("#{redisTemplate}")
    RedisTemplate<String, Object> redisTemplate;


    public Set<String> getKeysLike(String keyLike) {
        return redisTemplate.keys(keyLike);
    }

    public Set<Object> getHashKeys(String key) {
        return redisTemplate.opsForHash().keys(key);
    }
}
