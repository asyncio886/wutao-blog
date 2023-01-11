package top.wytbook.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.wytbook.constant.UserType;
import top.wytbook.constant.ValidatorRegx;
import top.wytbook.db.User;
import top.wytbook.dto.PageDto;
import top.wytbook.dto.Result;
import top.wytbook.dto.SafeUser;
import top.wytbook.service.UserService;
import top.wytbook.util.Md5Utils;
import top.wytbook.util.OssUtils;
import top.wytbook.util.RandomUtils;
import top.wytbook.util.TokenUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Pattern;

/**
 * 管理员对用户的设置
 */
@RestController
@RequestMapping("/api/admin")
@Validated
public class AdminUserController {
    @Autowired
    UserService userService;

    @Value("#{randomUtils}")
    RandomUtils randomUtils;

    @Value("#{ossUtils}")
    OssUtils ossUtils;
    @PostMapping(value = "/login",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result adminLogin(@Pattern(regexp = ValidatorRegx.EMAIL_REGX) @RequestParam("email") String email,
                             @RequestParam("password") String password,
                             HttpServletResponse response) {
        User user = userService.getUser(email, password);
        if (user == null) {
            return Result.operatorFail("邮箱或密码错误");
        }
        if (!user.getUserType().equals(UserType.ADMIN)) {
            return Result.operatorFail("非管理员用户");
        }
        TokenUtils.createToken(user.getUid(), response, 100);
        return Result.operatorOk("管理员登录成功");
    }

    @GetMapping("/users")
    public Result showUsers(@RequestParam("pn") Integer pn,
                            @RequestParam("size") Integer size,
                            @RequestParam(value = "admin",required = false, defaultValue = "false") Boolean needAdmin) {
        PageDto<SafeUser> userPage;
        try {
            userPage = userService.getUserPage(pn, size, needAdmin);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.operatorFail("用户列表获取失败");
        }
        return Result.operatorOk("ok", userPage);
    }

    @PostMapping(value = "/updateUserInfo/{uid}",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result updateUserInfo(@PathVariable("uid") Long uid,
                                 @RequestParam(value = "email",required = false) String email,
                                 @RequestParam(value = "password",required = false) String password,
                                 @RequestParam(value = "username",required = false) String username) {
        if (userService.existByUsername(username)) {
            return Result.operatorFail("用户名已被使用");
        }
        if (userService.existByEmail(email)) {
            return Result.operatorFail("邮箱已被使用");
        }
        User user = new User();
        user.setUid(uid);
        user.setEmail(email);
        user.setUsername(username);
        String salt = randomUtils.getRandomCode(10);
        user.setPassword(Md5Utils.getMd5String(password, salt));
        user.setSalt(salt);
        boolean updated = userService.updateById(user);
        if (updated) {
            return Result.operatorOk("更新成功");
        }
        return Result.operatorFail("更新失败");
    }

    @GetMapping("/userDelete/{uid}")
    public Result deleteUser(@PathVariable("uid") Long uid) {
        User user = userService.getById(uid);
        if (user != null && user.getUserType().equals(UserType.ADMIN)) {
            return Result.operatorFail("管理员用户无法控制台删除");
        }
        if (userService.removeById(uid)) {
            return Result.operatorOk("删除成功");
        }
        return Result.operatorFail("删除失败");
    }
    @PostMapping(value = "/avatar/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result uploadAdminUserAvatar(@RequestParam("file") MultipartFile file) {
        String avatarPath = ossUtils.uploadAdminUserAvatar(file);
        if (avatarPath != null) {
            return Result.operatorOk("上传成功", avatarPath);
        }
        return Result.operatorFail("上传失败");
    }
}
