package top.wytbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import top.wytbook.constant.AttributeKey;
import top.wytbook.db.Reply;
import top.wytbook.dto.Result;
import top.wytbook.service.ReplyService;

@RestController
@RequestMapping("/api/reply")
public class ReplyController {

    @Autowired
    ReplyService replyService;

    @PostMapping(value = "/upload",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result addReply(@RequestAttribute(AttributeKey.UID) Long uid,
                           @RequestParam("content") String content,
                           @RequestParam("cid") Long cid,
                           @RequestParam(value = "reply_to", required = false) Long replyUid) {
        Reply reply = new Reply();
        reply.setContent(content);
        reply.setCreateTime(System.currentTimeMillis());
        reply.setFromCid(cid);
        reply.setReplyToUid(replyUid);
        reply.setAuthorUid(uid);
        if (replyService.save(reply)) {
            return Result.operatorOk("评论成功");
        }
        return Result.operatorFail("评论失败");
    }


    @GetMapping("/list")
    public Result getReplies(@RequestParam("pn") Integer pn,
                             @RequestParam("size") Integer size,
                             @RequestParam("cid") Long cid) {
        return Result.operatorOk("ok", replyService.getReplies(cid, pn, size));
    }
}
