package top.wytbook.dto;

import lombok.Data;
import top.wytbook.db.Reply;

@Data
public class ReplyWithReplyToAndAuthor {
    Reply reply;
    NormalUser authorInfo;
    NormalUser replyToInfo;
}
