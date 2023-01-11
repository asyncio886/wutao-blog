package top.wytbook.dto;

import lombok.Data;

@Data
public class SimpleArticle {
    private Long aid;
    private String title;
    private Long createTime;
    private Long modifiedTime;
    private Integer likeCount;
    private Integer watchCount;
    private Long tagId;
    private String description;
    private Long ownUid;
    private String facePicture;
}
