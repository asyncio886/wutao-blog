package top.wytbook.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import top.wytbook.constant.AttributeKey;
import top.wytbook.db.AdminUser;
import top.wytbook.dto.Result;
import top.wytbook.service.AdminUserService;

@RestController
@RequestMapping("/api/admin/user")
public class AdminUserInfoController {

    @Autowired
    AdminUserService adminUserService;


    public static final String EMPTY_STR = "";

    @PostMapping(value = "/update",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result updateAdminUserInfo(@RequestAttribute(AttributeKey.UID) Long uid,
                                      @RequestParam(value = "avatar", required = false, defaultValue = EMPTY_STR) String avatar,
                                      @RequestParam(value = "description", required = false, defaultValue = EMPTY_STR) String description,
                                      @RequestParam(value = "qqLink",required = false,defaultValue = EMPTY_STR) String qqLink,
                                      @RequestParam(value = "twitterLink",required = false,defaultValue = EMPTY_STR) String twitterLink,
                                      @RequestParam(value = "weiboLink", required = false, defaultValue = EMPTY_STR) String weiboLink,
                                      @RequestParam(value = "githubLink", required = false, defaultValue = EMPTY_STR) String githubLink) {
        AdminUser adminUser = adminUserService.getById(uid);
        boolean st;
        if (adminUser == null) {
            adminUser = new AdminUser();
            adminUser.setUid(uid);
            adminUser.setAvatar(avatar);
            adminUser.setDescription(description);
            adminUser.setGithubLink(githubLink);
            adminUser.setQqLink(qqLink);
            adminUser.setTwitterLink(twitterLink);
            adminUser.setWeiboLink(weiboLink);
            st = adminUserService.save(adminUser);
        }
        else {
            if (adminUser.getAvatar() == null || !avatar.equals(EMPTY_STR)) {
                adminUser.setAvatar(avatar);
            }
            if (adminUser.getDescription() == null || !description.equals(EMPTY_STR)) {
                adminUser.setDescription(description);
            }
            if (adminUser.getGithubLink() == null || !githubLink.equals(EMPTY_STR)) {
                adminUser.setGithubLink(githubLink);
            }
            if (adminUser.getQqLink() == null || !qqLink.equals(EMPTY_STR)) {
                adminUser.setQqLink(qqLink);
            }
            if (adminUser.getTwitterLink() == null || !twitterLink.equals(EMPTY_STR)) {
                adminUser.setTwitterLink(twitterLink);
            }
            if (adminUser.getWeiboLink() == null || !weiboLink.equals(EMPTY_STR)) {
                adminUser.setWeiboLink(weiboLink);
            }
            st = adminUserService.updateById(adminUser);
        }
        if (st) {
            return Result.operatorOk("更新成功");
        }
        return Result.operatorFail("更新失败");
    }
}
