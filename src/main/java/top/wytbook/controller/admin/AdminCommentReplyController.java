package top.wytbook.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.wytbook.constant.AttributeKey;
import top.wytbook.dto.Result;
import top.wytbook.service.ArticleService;
import top.wytbook.service.CommentService;
import top.wytbook.service.ReplyService;

@RestController
@RequestMapping("/api/admin")
public class AdminCommentReplyController {

    @Autowired
    CommentService commentService;

    @Autowired
    ReplyService replyService;

    @Autowired
    ArticleService articleService;

    @GetMapping("/deleteComment/{cid}")
    public Result deleteComment(@RequestAttribute(AttributeKey.UID) Long uid,
                                @PathVariable("cid") Long cid) {
        Long fromAid = commentService.getFromAid(cid);
        if (fromAid == null) {
            if (commentService.removeById(cid)) {
                return Result.operatorOk("原文章不存在，评论删除");
            }
            else {
                return Result.operatorFail("操作失败");
            }
        }
        Long ownUid = articleService.getOwnUid(fromAid);
        if (ownUid == null) {
            if (commentService.removeById(cid)) {
                return Result.operatorOk("评论作者不存在，评论删除");
            }
            return Result.operatorFail("操作失败");
        }
        if (ownUid.equals(uid)) {
            if (commentService.removeById(cid)) {
                return Result.operatorOk("操作成功");
            }
            return Result.operatorFail("操作失败");
        }
        else {
            return Result.operatorFail("管理员身份错误");
        }
    }

    @GetMapping("/deleteReply/{rid}")
    public Result deleteReply(@PathVariable("rid") Long rid,
                              @RequestAttribute(AttributeKey.UID) Long uid) {
        Long fromCid = replyService.getFromCid(rid);
        if (fromCid == null) {
            if (replyService.removeById(rid)) {
                return Result.operatorOk("原主评论不存在，回复删除");
            }
            return Result.operatorFail("操作失败");
        }
        Long fromAid = commentService.getFromAid(fromCid);
        if (fromAid == null) {
            if (replyService.removeById(rid)) {
                return Result.operatorOk("原文不存在，回复删除");
            }
            return Result.operatorFail("操作失败");
        }
        Long ownUid = articleService.getOwnUid(fromAid);
        if (ownUid == null) {
            if (replyService.removeById(rid)) {
                return Result.operatorOk("原评论作者不存在，回复删除");
            }
            return Result.operatorFail("操作失败");
        }
        if (ownUid.equals(uid)) {
            if (replyService.removeById(rid)) {
                return Result.operatorOk("操作成功");
            }
            return Result.operatorFail("操作失败");
        }
        else {
            return Result.operatorFail("管理员身份错误");
        }
    }
}
