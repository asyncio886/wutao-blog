package top.wytbook.dto;

import lombok.Data;

@Data
public class UserDetailDto {
    private Long uid;
    private String avatar;
    private String description;
    private String githubLink;
    private String qqLink;
    private String twitterLink;
    private String weiboLink;
    private String username;
}
