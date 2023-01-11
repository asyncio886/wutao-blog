package top.wytbook.es.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogArticleDao extends ElasticsearchRepository<BlogArticle, Long> {
    Page<BlogArticle> findByAllTextLike(String allText, Pageable pageable);
}
