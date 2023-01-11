package top.wytbook.dto;

import lombok.Data;
import top.wytbook.db.Comment;

@Data
public class CommentWithAuthorInfo {
    Comment comment;
    NormalUser userInfo;
    Long replySum;
}
