package top.wytbook.mapper;

import top.wytbook.db.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.wytbook.dto.SimpleArticleWithAuthorInfo;

import java.util.List;

/**
* @author lenovo
* @description 针对表【t_article】的数据库操作Mapper
* @createDate 2023-01-07 20:05:49
* @Entity top.wytbook.db.Article
*/
public interface ArticleMapper extends BaseMapper<Article> {
    List<SimpleArticleWithAuthorInfo> getSimpleArticleWithTagId(Long tagId, Integer start, Integer limit);
    List<SimpleArticleWithAuthorInfo> getNormalArticles(Integer start, Integer limit);
    List<SimpleArticleWithAuthorInfo> getHostArticles(Integer size);
}




