package top.wytbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import top.wytbook.db.Comment;
import top.wytbook.dto.CommentWithAuthorInfo;
import top.wytbook.dto.PageDto;
import top.wytbook.service.CommentService;
import top.wytbook.mapper.CommentMapper;
import org.springframework.stereotype.Service;
import top.wytbook.service.ReplyService;

import java.util.List;

/**
* @author lenovo
* @description 针对表【t_comment】的数据库操作Service实现
* @createDate 2023-01-07 18:07:02
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

    @Value("#{replyServiceImpl}")
    ReplyService replyService;

    @Override
    public PageDto<CommentWithAuthorInfo> getCommentsByAid(Long aid, Integer pn, Integer size) {
        List<CommentWithAuthorInfo> dataList = getBaseMapper().getCommentsWithUserInfo(aid, (pn - 1) * size, size);
        for (CommentWithAuthorInfo data : dataList) {
            data.setReplySum(replyService.getSumOfComment(data.getComment().getCid()));
        }
        PageDto<CommentWithAuthorInfo> res = new PageDto<>();
        res.setDataList(dataList);
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getFromAid, aid);
        res.setSum(count(queryWrapper));
        return res;
    }


    @Override
    public Long getFromAid(Long cid) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Comment::getFromAid).eq(Comment::getCid, cid);
        return getObj(queryWrapper, o -> (Long) o);
    }
}




