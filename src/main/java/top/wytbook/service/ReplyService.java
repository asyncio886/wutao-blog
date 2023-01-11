package top.wytbook.service;

import top.wytbook.db.Reply;
import com.baomidou.mybatisplus.extension.service.IService;
import top.wytbook.dto.PageDto;
import top.wytbook.dto.ReplyWithReplyToAndAuthor;

/**
* @author lenovo
* @description 针对表【t_reply】的数据库操作Service
* @createDate 2023-01-07 18:07:17
*/
public interface ReplyService extends IService<Reply> {
    Long getSumOfComment(Long cid);
    PageDto<ReplyWithReplyToAndAuthor> getReplies(Long cid, Integer pn, Integer size);
    Long getFromCid(Long rid);
}
