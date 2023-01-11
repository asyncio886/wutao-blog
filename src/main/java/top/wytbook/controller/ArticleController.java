package top.wytbook.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import top.wytbook.db.AdminUser;
import top.wytbook.db.Article;
import top.wytbook.db.User;
import top.wytbook.dto.ArticleWithAuthorInfo;
import top.wytbook.dto.NormalUser;
import top.wytbook.dto.PageDto;
import top.wytbook.dto.Result;
import top.wytbook.es.EsService;
import top.wytbook.es.dao.BlogArticle;
import top.wytbook.service.AdminUserService;
import top.wytbook.service.ArticleService;
import top.wytbook.service.TagService;
import top.wytbook.service.UserService;

import java.util.ArrayList;

/**
 * 对文章的查询
 */
@RestController
@RequestMapping("/api/article")
public class ArticleController {

    @Value("#{userServiceImpl}")
    UserService userService;

    @Value("#{articleServiceImpl}")
    ArticleService articleService;

    @Value("#{adminUserServiceImpl}")
    AdminUserService adminUserService;

    @Value("#{tagServiceImpl}")
    TagService tagService;

    @Value("#{esService}")
    EsService esService;

    @GetMapping("/detail/{aid}")
    public Result getArticleDetail(@PathVariable("aid") Long aid) {
        Article article = articleService.getById(aid);
        if (article == null) {
            return Result.operatorFail("文章不存在");
        }
        articleService.filterWithCache(article);
        Long ownUid = article.getOwnUid();
        AdminUser adminUserInfo = adminUserService.getById(ownUid);
        User user = userService.getById(ownUid);
        ArticleWithAuthorInfo articleWithAuthorInfo = new ArticleWithAuthorInfo();
        articleWithAuthorInfo.setArticle(article);
        articleWithAuthorInfo.setDetailUserInfo(adminUserInfo);
        NormalUser normalUser = new NormalUser();
        BeanUtils.copyProperties(user, normalUser);
        articleWithAuthorInfo.setBaseUserInfo(normalUser);
        articleWithAuthorInfo.setTag(tagService.getById(article.getTagId()));
        articleService.addCacheWatch(aid);
        return Result.operatorOk("ok", articleWithAuthorInfo);
    }

    @GetMapping("/listByTagId")
    public Result getArticlesByTagId(@RequestParam("tagId") Long tag,
                                     @RequestParam("pn") Integer pn,
                                     @RequestParam("size") Integer size) {
        return Result.operatorOk("ok", articleService.getArticlesWithTagId(tag, pn, size));
    }

    @GetMapping("/list")
    public Result getArticlesNormal(@RequestParam("pn") Integer pn,
                                    @RequestParam("size") Integer size) {

        return Result.operatorOk("ok", articleService.getNormalArticles(pn, size));
    }

    /**
     * 最热得到size篇文章
     * @return
     */
    @GetMapping("/listHot")
    public Result getHotArticles(@RequestParam("size") Integer size) {
        return Result.operatorOk("ok", articleService.getHotArticles(size));
    }

    @GetMapping("/tags")
    public Result getTags() {
        return Result.operatorOk("ok", tagService.getAllTags());
    }

    @GetMapping("/search")
    public Result searchArticle(@RequestParam("kw") String kw,
                                @RequestParam("pn") Integer pn) {
        kw = kw.trim();
        if (kw.equals("") || pn > 10) {
            PageDto<BlogArticle> res = new PageDto<>();
            res.setSum(0L);
            res.setDataList(new ArrayList<>());
            return Result.operatorOk("ok", res);
        }
        return Result.operatorOk("ok", esService.search(kw, pn));
    }
}
