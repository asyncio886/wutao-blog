package top.wytbook.service;

import top.wytbook.db.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import top.wytbook.dto.CommentWithAuthorInfo;
import top.wytbook.dto.PageDto;

/**
* @author lenovo
* @description 针对表【t_comment】的数据库操作Service
* @createDate 2023-01-07 18:07:02
*/
public interface CommentService extends IService<Comment> {
    PageDto<CommentWithAuthorInfo> getCommentsByAid(Long aid, Integer pn, Integer size);
    Long getFromAid(Long cid);
}
