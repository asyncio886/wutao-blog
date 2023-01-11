package top.wytbook.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import top.wytbook.mq.LikeMqHandler;
import top.wytbook.mq.WatchMqHandler;
import top.wytbook.service.ArticleService;
import top.wytbook.service.LikeService;
import top.wytbook.util.RedisUtils;

import java.util.Set;

/**
 * 刷缓存里面的点赞，浏览数据到数据库
 */
@Component
public class UpdateCacheToDb {
    @Value("#{redisUtils}")
    RedisUtils redisUtils;

    @Value("#{redisTemplate}")
    RedisTemplate<String, Object> redisTemplate;

    @Value("#{redisCountTemplate}")
    RedisTemplate<String, Long> redisCountTemplate;

    @Value("#{articleServiceImpl}")
    ArticleService articleService;

    @Autowired
    LikeService likeService;
    @Transactional
    public void flushWatchCountToDb() {
        Set<Object> hashKeys = redisUtils.getHashKeys(WatchMqHandler.WATCH_KEY);
        for (Object hashKey : hashKeys) {
            String count = (String) redisCountTemplate.opsForHash().get(WatchMqHandler.WATCH_KEY, hashKey);
            redisCountTemplate.opsForHash().delete(WatchMqHandler.WATCH_KEY, hashKey);
            if (count != null) {
                long increment = Long.parseLong(count);
                Long aid = Long.valueOf((String) hashKey);
                if (increment > 0L) {
                    articleService.addCount(aid, increment);
                }
            }
        }
    }

    /**
     * likeRecord留在redis里面没关系
     */
    @Transactional
    public void flushLikeRecordToDb() {
        Set<String> keys = redisUtils.getKeysLike("like:*");
        for (String key : keys) {
            Long aid = Long.valueOf(key.split(":")[1]);
            Set<Object> hashKeys = redisUtils.getHashKeys(key);
            for (Object hashKey : hashKeys) {
                Long uid = Long.valueOf((String) hashKey);
                Object o = redisCountTemplate.opsForHash().get(key, hashKey);
                if (o != null) {
                    Integer status = Integer.valueOf((String) o);
                    redisTemplate.opsForHash().delete(key, hashKey);
                    likeService.addLikeRecord(aid, uid, status);
                }
            }
        }
    }

    @Transactional
    public void flushLikeCountToDb() {
        Set<Object> hashKeys = redisUtils.getHashKeys(LikeMqHandler.LIKE_COUNT_KEY);
        for (Object hashKey : hashKeys) {
            Object o = redisCountTemplate.opsForHash().get(LikeMqHandler.LIKE_COUNT_KEY, hashKey);
            redisCountTemplate.opsForHash().delete(LikeMqHandler.LIKE_COUNT_KEY, hashKey);
            if (o != null) {
                Long aid = Long.valueOf((String) hashKey);
                Long count = Long.valueOf((String) o);
                articleService.addLike(aid, count);
            }
        }
    }
}
