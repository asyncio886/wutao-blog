package top.wytbook.dto;

import lombok.Data;

@Data
public class SafeUser {
    private Long uid;
    private String email;
    private String username;
    private Long createTime;
    private Integer userType;
}
