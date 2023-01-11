package top.wytbook.dto;

import lombok.Data;

@Data
public class SimpleArticleWithAuthorInfo {
    SimpleArticle simpleArticle;
    NormalUser userInfo;
}
