package top.wytbook.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class ClearCacheAfterSave {
    @Value("#{redisTemplate}")
    RedisTemplate<String, Object> redisTemplate;

    public void remove(String... keys) {
        redisTemplate.delete(String.join("::", keys));
    }
}
