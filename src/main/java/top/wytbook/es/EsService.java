package top.wytbook.es;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import top.wytbook.dto.PageDto;
import top.wytbook.es.dao.BlogArticle;
import top.wytbook.es.dao.BlogArticleDao;

/**
 * es数据库必须是t_blog_article
 */
@Component
public class EsService {

    @Autowired
    BlogArticleDao dao;
    // TODO es接入
    public void insertArticle(Long aid, String title, String description) {
        BlogArticle blogArticle = new BlogArticle();
        blogArticle.setTitle(title);
        blogArticle.setDescription(description);
        blogArticle.setAid(aid);
        dao.save(blogArticle);
    }

    public void updateArticle(Long aid, String title, String description) {
        BlogArticle blogArticle = new BlogArticle();
        blogArticle.setTitle(title);
        blogArticle.setDescription(description);
        blogArticle.setAid(aid);
        dao.save(blogArticle);
    }

    public void removeArticle(Long aid) {
        dao.deleteById(aid);
    }

    public PageDto<BlogArticle> search(String text, Integer pn) {
        Pageable pageable = PageRequest.of(pn - 1, 6);
        PageDto<BlogArticle> res = new PageDto<>();
        Page<BlogArticle> blogArticlePage = dao.findByAllTextLike(text, pageable);
        res.setDataList(blogArticlePage.stream().toList());
        res.setSum(blogArticlePage.getTotalElements());
        return res;
    }
}
