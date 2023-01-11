package top.wytbook.controller.admin;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.wytbook.constant.AttributeKey;
import top.wytbook.db.Article;
import top.wytbook.db.Tag;
import top.wytbook.dto.ArticleVo;
import top.wytbook.dto.Result;
import top.wytbook.service.ArticleService;
import top.wytbook.service.TagService;
import top.wytbook.es.EsService;
import top.wytbook.util.OssUtils;

import javax.validation.Valid;

/**
 * 管理员添加文章，删除文章接口
 */
@RestController
@RequestMapping("/api/admin/article")
public class AdminArticleController {

    @Value("#{articleServiceImpl}")
    ArticleService articleService;

    @Value("#{esService}")
    EsService esService;

    @Value("#{ossUtils}")
    OssUtils ossUtils;

    @Value("#{tagServiceImpl}")
    TagService tagService;

    @PostMapping(value = "/upload",consumes = MediaType.APPLICATION_JSON_VALUE)
    public Result uploadArticle(@RequestBody @Valid ArticleVo articleVo, @RequestAttribute(AttributeKey.UID) Long uid) {
        Tag tag = tagService.getById(articleVo.getTagId());
        if (tag == null) {
            return Result.operatorFail("标签不存在");
        }
        Article article = new Article();
        BeanUtils.copyProperties(articleVo, article);
        article.setOwnUid(uid);
        long time = System.currentTimeMillis();
        article.setCreateTime(time);
        article.setModifiedTime(time);
        boolean saved = articleService.save(article);
        if (saved) {
            esService.insertArticle(article.getAid(), article.getTitle(), article.getDescription());
            return Result.operatorOk("上传成功");
        }
        return Result.operatorFail("上传失败");
    }

    @GetMapping("/delete")
    public Result deleteArticle(@RequestParam("aid") Long aid, @RequestAttribute(AttributeKey.UID) Long uid) {
        if (articleService.deleteByAidAndUid(aid, uid)) {
            esService.removeArticle(aid);
            return Result.operatorOk("删除成功");
        }
        return Result.operatorFail("删除失败");
    }

    @PostMapping(value = "/update/{aid}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public Result updateArticle(@RequestAttribute(AttributeKey.UID) Long uid,
                                @PathVariable("aid") Long aid,
                                @RequestBody @Valid ArticleVo articleVo) {
        Tag tag = tagService.getById(articleVo.getTagId());
        if (tag == null) {
            return Result.operatorFail("标签不存在");
        }
        if (!articleService.existArticle(aid, uid)) {
            return Result.operatorFail("文章不存在");
        }
        Article article = new Article();
        BeanUtils.copyProperties(articleVo, article);
        article.setAid(aid);
        article.setOwnUid(uid);
        article.setModifiedTime(System.currentTimeMillis());
        article.setFacePicture(articleVo.getFacePicture());
        if (articleService.updateById(article)) {
            esService.updateArticle(aid, article.getTitle(), article.getDescription());
            return Result.operatorOk("更新成功");
        }
        return Result.operatorFail("更新失败");
    }

    @PostMapping(value = "/uploadPicture",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result uploadPicture(@RequestParam("file") MultipartFile file) {
        String url = ossUtils.uploadArticlePictureToOss(file);
        if (url != null) {
            return Result.operatorOk("上传成功" , url);
        }
        return Result.operatorFail("上传失败");
    }
}
