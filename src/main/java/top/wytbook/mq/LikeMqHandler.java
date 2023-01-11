package top.wytbook.mq;

import lombok.Data;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.wytbook.config.MqConfig;
import top.wytbook.service.LikeService;

@Component
@RabbitListener(queues = MqConfig.LIKE_QUEUE)
public class LikeMqHandler {
    @Value("#{likeServiceImpl}")
    LikeService likeService;

    @Data
    public static class LikeMessage {
        Long aid;
        Long uid;
        Boolean status;
    }
    @Value("#{redisTemplate}")
    RedisTemplate<String, Object> redisTemplate;
    static final String LIKE_KEY_PREFIX = "like:";
    public static final String LIKE_COUNT_KEY = "like_count";
    public static String getHashKey(Long aid) {
        return LIKE_KEY_PREFIX + aid;
    }
    public void doLike(Long uid, Long aid) {
        redisTemplate.opsForHash().put(getHashKey(aid), String.valueOf(uid), 1);
        redisTemplate.opsForHash().increment(LIKE_COUNT_KEY, String.valueOf(aid), 1L);
    }

    public void doCancelLike(Long uid, Long aid) {
        redisTemplate.opsForHash().put(getHashKey(aid), String.valueOf(uid), 0);
        redisTemplate.opsForHash().increment(LIKE_COUNT_KEY, String.valueOf(aid), -1L);
    }


    @RabbitHandler
    public void likeHandler(LikeMessage likeMessage) {
        try {
            if (likeMessage.getStatus()) {
                doLike(likeMessage.getUid(), likeMessage.getAid());
            }
            else {
                doCancelLike(likeMessage.getUid(), likeMessage.getAid());
            }
        }
        catch (Exception ignored) {

        }
    }
}
