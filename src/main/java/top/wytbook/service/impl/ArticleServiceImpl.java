package top.wytbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import top.wytbook.mq.LikeMqHandler;
import top.wytbook.config.MqConfig;
import top.wytbook.mq.WatchMqHandler;
import top.wytbook.constant.CacheStore;
import top.wytbook.db.Article;
import top.wytbook.dto.PageDto;
import top.wytbook.dto.SimpleArticleWithAuthorInfo;
import top.wytbook.service.ArticleService;
import top.wytbook.mapper.ArticleMapper;
import org.springframework.stereotype.Service;
import top.wytbook.util.ClearCacheAfterSave;

import java.io.Serializable;
import java.util.List;

/**
* @author lenovo
* @description 针对表【t_article】的数据库操作Service实现
* @createDate 2023-01-07 20:05:49
*/
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService{

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    RedisTemplate<String, Long> redisCountTemplate;

    @Autowired
    ClearCacheAfterSave clearCacheAfterSave;

    @Override
    @CacheEvict(value = CacheStore.ARTICLE_PREFIX, key = "aid")
    public boolean deleteByAidAndUid(Long aid, Long uid) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getAid, aid).eq(Article::getOwnUid, uid);
        return remove(queryWrapper);
    }

    @Override
    public boolean existArticle(Long aid, Long uid) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getAid, aid).eq(Article::getOwnUid, uid);
        return getBaseMapper().exists(queryWrapper);
    }

    @Override
    public PageDto<SimpleArticleWithAuthorInfo> getArticlesWithTagId(Long tagId, Integer pn, Integer size) {
        ArticleMapper baseMapper = getBaseMapper();
        List<SimpleArticleWithAuthorInfo> simpleArticles = baseMapper.getSimpleArticleWithTagId(tagId, (pn - 1) * size, size);
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getTagId, tagId);
        long count = count(queryWrapper);
        PageDto<SimpleArticleWithAuthorInfo> res = new PageDto<>();
        res.setSum(count);
        res.setDataList(simpleArticles);
        return res;
    }

    @Override
    public PageDto<SimpleArticleWithAuthorInfo> getNormalArticles(Integer pn, Integer size) {
        ArticleMapper baseMapper = getBaseMapper();
        List<SimpleArticleWithAuthorInfo> articles = baseMapper.getNormalArticles((pn - 1) * size, size);
        long count = count();
        PageDto<SimpleArticleWithAuthorInfo> res = new PageDto<>();
        res.setSum(count);
        res.setDataList(articles);
        return res;
    }

    @Override
    public PageDto<SimpleArticleWithAuthorInfo> getHotArticles(Integer size) {
        PageDto<SimpleArticleWithAuthorInfo> res = new PageDto<>();
        res.setSum(Long.valueOf(size));
        res.setDataList(baseMapper.getHostArticles(size));
        return res;
    }

    @Override
    public void addCacheWatch(Long aid) {
        rabbitTemplate.convertAndSend(MqConfig.SIMPLE_TASK_EXCHANGE, MqConfig.WATCH_ROUTE, aid);
    }

    @Override
    public Long getCacheWatchNumber(Long aid) {
        Object o = redisCountTemplate.opsForHash().get(WatchMqHandler.WATCH_KEY, String.valueOf(aid));
        if (o == null) {
            return 0L;
        }
        return Long.valueOf((String) o);
    }

    @Override
    public Long getCacheLikeNumber(Long aid) {
        Object o = redisCountTemplate.opsForHash().get(LikeMqHandler.LIKE_COUNT_KEY, String.valueOf(aid));
        if (o == null) {
            return 0L;
        }
        return Long.valueOf((String) o);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheStore.ARTICLE_PREFIX, key = "#aid")
    public void addCount(Long aid, Long add) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Article::getWatchCount).eq(Article::getAid, aid);
        Integer count = getObj(queryWrapper, (o) -> (Integer) o);
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getAid, aid).set(Article::getWatchCount, add + count);
        update(updateWrapper);
    }

    @Override
    @Transactional
    @CacheEvict(value = CacheStore.ARTICLE_PREFIX, key = "#aid")
    public void addLike(Long aid, Long count) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Article::getLikeCount).eq(Article::getAid, aid);
        Integer dbCount = getObj(queryWrapper, (o) -> (Integer) o);
        if (dbCount != null) {
            LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Article::getAid, aid).set(Article::getLikeCount, count + dbCount);
            update(updateWrapper);
        }
    }

    @Override
    public void filterWithCache(Article article) {
        article.setLikeCount(Math.toIntExact(getCacheLikeNumber(article.getAid())) + article.getLikeCount());
        article.setWatchCount(Math.toIntExact(getCacheWatchNumber(article.getAid())) + article.getWatchCount());
    }

    @Override
    @Cacheable(value = CacheStore.ARTICLE_PREFIX, key = "#id")
    public Article getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public boolean save(Article entity) {
        boolean st = super.save(entity);
        if (st) {
            clearCacheAfterSave.remove(CacheStore.ARTICLE_PREFIX, String.valueOf(entity.getAid()));
        }
        return st;
    }

    @Override
    @CacheEvict(value = CacheStore.ARTICLE_PREFIX, key = "#entity.aid")
    public boolean updateById(Article entity) {
        return super.updateById(entity);
    }

    @Override
    public Long getOwnUid(Long aid) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Article::getOwnUid).eq(Article::getAid, aid);
        return getObj(queryWrapper, o-> (Long) o);
    }
}




