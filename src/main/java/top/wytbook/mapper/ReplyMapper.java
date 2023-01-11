package top.wytbook.mapper;

import top.wytbook.db.Reply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.wytbook.dto.ReplyWithReplyToAndAuthor;

import java.util.List;

/**
* @author lenovo
* @description 针对表【t_reply】的数据库操作Mapper
* @createDate 2023-01-07 18:07:17
* @Entity top.wytbook.db.Reply
*/
public interface ReplyMapper extends BaseMapper<Reply> {
    List<ReplyWithReplyToAndAuthor> getRepliesByCid(Long cid, Integer start, Integer limit);
}




