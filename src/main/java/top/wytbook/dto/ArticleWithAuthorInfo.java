package top.wytbook.dto;

import lombok.Data;
import top.wytbook.db.AdminUser;
import top.wytbook.db.Article;
import top.wytbook.db.Tag;

@Data
public class ArticleWithAuthorInfo {
    Article article;
    NormalUser baseUserInfo;
    AdminUser detailUserInfo;
    Tag tag;
}
