package top.wytbook.es.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "t_blog_article")
public class BlogArticle {
    @Id
    Long aid;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", copyTo = "allText")
    String description;
    @Field(type = FieldType.Text, analyzer = "ik_max_word", copyTo = "allText")
    String title;
    @Field(type = FieldType.Text)
    String allText;
}
