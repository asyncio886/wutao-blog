package top.wytbook.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import top.wytbook.constant.AttributeKey;
import top.wytbook.db.Comment;
import top.wytbook.dto.Result;
import top.wytbook.service.CommentService;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Value("#{commentServiceImpl}")
    CommentService commentService;

    @PostMapping(value = "/upload",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result addComment(@RequestAttribute(AttributeKey.UID) Long uid,
                             @RequestParam("content") String content,
                             @RequestParam("aid") Long aid) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setAuthorUid(uid);
        comment.setCreateTime(System.currentTimeMillis());
        comment.setFromAid(aid);
        if (commentService.save(comment)) {
            return Result.operatorOk("评论成功");
        }
        return Result.operatorFail("评论失败");
    }

    @GetMapping("/list/{aid}")
    public Result getComments(@RequestParam("pn") Integer pn,
                              @RequestParam("size") Integer size,
                              @PathVariable("aid") Long aid) {
        return Result.operatorOk("ok", commentService.getCommentsByAid(aid, pn, size));
    }
}
