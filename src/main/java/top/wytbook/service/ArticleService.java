package top.wytbook.service;

import top.wytbook.db.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import top.wytbook.dto.PageDto;
import top.wytbook.dto.SimpleArticleWithAuthorInfo;

/**
* @author lenovo
* @description 针对表【t_article】的数据库操作Service
* @createDate 2023-01-07 20:05:49
*/
public interface ArticleService extends IService<Article> {
    boolean deleteByAidAndUid(Long aid, Long uid);
    boolean existArticle(Long aid, Long uid);
    PageDto<SimpleArticleWithAuthorInfo> getArticlesWithTagId(Long tagId, Integer pn, Integer size);
    PageDto<SimpleArticleWithAuthorInfo> getNormalArticles(Integer pn, Integer size);
    PageDto<SimpleArticleWithAuthorInfo> getHotArticles(Integer size);
    Long getCacheWatchNumber(Long aid);
    Long getCacheLikeNumber(Long aid);
    void addCount(Long aid, Long add);
    void addCacheWatch(Long aid);
    void addLike(Long aid, Long count);
    // 和缓存数据同步
    void filterWithCache(Article article);
    Long getOwnUid(Long aid);
}
