package top.wytbook.mq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.wytbook.config.MqConfig;

@Component
@RabbitListener(queues = MqConfig.WATCH_QUEUE)
public class WatchMqHandler {

    @Value("#{redisTemplate}")
    RedisTemplate<String, Object> redisTemplate;

    public static final String WATCH_KEY = "cache_watch";

    public void increment(Long aid) {
        redisTemplate.opsForHash().increment(WATCH_KEY, String.valueOf(aid), 1L);
    }

    @RabbitHandler
    public void handlerAddWatch(Long targetAid) {
        try {
            increment(targetAid);
        }
        catch (Exception ignored) {

        }
    }
}
