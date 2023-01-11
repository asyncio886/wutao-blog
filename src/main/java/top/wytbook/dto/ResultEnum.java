package top.wytbook.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultEnum {
    NOT_ADMIN(1,"权限不足"),
    NEED_LOGIN(2,"需要登录"),
    UNKNOWN_ERROR(3,"未知错误"),
    IP_ATTACK(4, "你的访问请求过于频繁，服务器选择制裁你"),
    IP_NOT_FIND(5, "你的ip解析错误，请更换网络"),
    NOT_FOUND_SERVICE(6,"服务不存在"),
    FILE_SIZE_ERROR(7,"文件大小超过限制"),
    VALIDATOR_ERROR(8, "参数错误");
    final Integer code;
    final String message;
}
