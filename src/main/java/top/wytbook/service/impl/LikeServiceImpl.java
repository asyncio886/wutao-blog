package top.wytbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jetbrains.annotations.Nullable;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.wytbook.mq.LikeMqHandler;
import top.wytbook.config.MqConfig;
import top.wytbook.db.Like;
import top.wytbook.mapper.LikeMapper;
import top.wytbook.service.LikeService;

import static top.wytbook.mq.LikeMqHandler.getHashKey;

/**
* @author lenovo
* @description 针对表【t_like】的数据库操作Service实现
* @createDate 2023-01-07 18:07:12
*/
@Service
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like>
    implements LikeService{

    @Autowired
    RabbitTemplate rabbitTemplate;
    @Value("#{redisTemplate}")
    RedisTemplate<String, Object> redisTemplate;


    @Override
    public boolean articleLike(Boolean st, Long fromUid, Long targetId) {
        try {
            if (st) {
                if (hasLiked(fromUid, targetId)) {
                    return false;
                }
            }
            else {
                if (!hasLiked(fromUid, targetId)) {
                    return false;
                }
            }
            LikeMqHandler.LikeMessage message = new LikeMqHandler.LikeMessage();
            message.setAid(targetId);
            message.setUid(fromUid);
            message.setStatus(st);
            rabbitTemplate.convertAndSend(MqConfig.SIMPLE_TASK_EXCHANGE, MqConfig.LIKE_ROUTE, message);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Nullable
    private Integer cacheLiked(Long fromUid, Long targetId) {
        return (Integer) redisTemplate.opsForHash().get(getHashKey(targetId), String.valueOf(fromUid));
    }

    @Override
    public boolean hasLiked(Long fromUid, Long targetId) {
        Integer status = cacheLiked(fromUid, targetId);
        if (status != null) {
            return status == 1;
        }
        else {
            LambdaQueryWrapper<Like> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Like::getFromUid, fromUid).eq(Like::getTargetId, targetId);
            boolean exists = getBaseMapper().exists(queryWrapper);
            // 更新缓存里面用户的点赞状态
            if (exists) {
                redisTemplate.opsForHash().put(getHashKey(targetId), String.valueOf(fromUid), 1);
            }
            else {
                redisTemplate.opsForHash().put(getHashKey(targetId), String.valueOf(fromUid), 0);
            }
            return exists;
        }
    }

    @Override
    public void addLikeRecord(Long aid, Long uid, Integer status) {
        LambdaQueryWrapper<Like> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Like::getFromUid, uid).eq(Like::getTargetId, aid);
        if (status == 1) {
            if (!getBaseMapper().exists(queryWrapper)) {
                Like like = new Like();
                like.setFromUid(uid);
                like.setTargetId(aid);
                save(like);
            }
        }
        else {
            remove(queryWrapper);
        }
    }
}




