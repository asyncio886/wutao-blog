package top.wytbook.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import top.wytbook.constant.AttributeKey;
import top.wytbook.dto.Result;
import top.wytbook.service.LikeService;

@RestController
@RequestMapping("/api/like")
public class LikeController {

    @Value("#{likeServiceImpl}")
    LikeService likeService;
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result doLike(@RequestParam("status") Boolean status,
                         @RequestParam("aid") Long aid,
                         @RequestAttribute(AttributeKey.UID) Long uid) {
        if (likeService.articleLike(status, uid, aid)) {
            return Result.operatorOk("ok");
        }
        return Result.operatorFail("操作失败");
    }
}
