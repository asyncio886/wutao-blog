package top.wytbook.dto;

import lombok.Data;

@Data
public class NormalUser {
    private Long uid;
    private String username;
    private Long createTime;
    private Integer userType;
}
