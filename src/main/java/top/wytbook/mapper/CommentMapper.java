package top.wytbook.mapper;

import top.wytbook.db.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.wytbook.dto.CommentWithAuthorInfo;

import java.util.List;

/**
* @author lenovo
* @description 针对表【t_comment】的数据库操作Mapper
* @createDate 2023-01-07 18:07:02
* @Entity top.wytbook.db.Comment
*/
public interface CommentMapper extends BaseMapper<Comment> {
    List<CommentWithAuthorInfo> getCommentsWithUserInfo(Long aid, Integer start, Integer limit);
}




