package top.wytbook.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.wytbook.constant.ValidatorRegx;
import top.wytbook.db.AdminUser;
import top.wytbook.db.User;
import top.wytbook.dto.Result;
import top.wytbook.dto.UserDetailDto;
import top.wytbook.service.AdminUserService;
import top.wytbook.service.UserService;
import top.wytbook.util.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

/**
 * 处理用户登录
 */
@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {

    @Value("#{codeUtils}")
    CodeUtils codeUtils;

    @Value("#{randomUtils}")
    RandomUtils randomUtils;

    @Value("#{emailUtils}")
    EmailUtils emailUtils;

    @Value("#{userServiceImpl}")
    UserService userService;

    @Value("#{adminUserServiceImpl}")
    AdminUserService adminUserService;

    public static final Integer CODE_TIME_EXPIRE = 60 * 2;
    public static final Integer CODE_LENGTH = 5;


    @GetMapping("/getCode")
    public Result getCode(@Pattern(regexp = ValidatorRegx.EMAIL_REGX) @RequestParam("email") String email) {
        if (userService.existByEmail(email)) {
            return Result.operatorFail("邮箱已经被注册");
        }
        if (codeUtils.getCacheCode(email) != null) {
            return Result.operatorFail("已经获取过了验证码，请稍后重试");
        }
        String code = randomUtils.getRandomCode(CODE_LENGTH);
        codeUtils.setCodeToCache(email, code, CODE_TIME_EXPIRE);
        emailUtils.sendEmail(email, "验证码",
                code + " , " + (CODE_TIME_EXPIRE / 60) + "分钟内有效");
        return Result.operatorOk("验证码发送成功，注意查收");
    }


    /**
     * 注册
     * @return
     */
    @PostMapping(value = "/register",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result register(@Pattern(regexp = ValidatorRegx.EMAIL_REGX) @RequestParam("email") String email,
                           @RequestParam("password") String password,
                           @RequestParam("username") String username,
                           @RequestParam("code") String code,
                           HttpServletResponse response) {
        if (userService.existByUsername(username)) {
            return Result.operatorFail("用户名已经被注册");
        }
        String cacheCode = codeUtils.getCacheCode(email);
        if (cacheCode == null) {
            return Result.operatorFail("验证码过期或为申请");
        }
        if (!cacheCode.equals(code)) {
            return Result.operatorFail("验证码错误");
        }

        User user = new User();
        user.setCreateTime(System.currentTimeMillis());
        String salt = randomUtils.getRandomCode(10);
        user.setPassword(Md5Utils.getMd5String(password, salt));
        user.setSalt(salt);
        user.setEmail(email);
        user.setUsername(username);
        boolean saved = userService.save(user);
        if (saved) {
            TokenUtils.createToken(user.getUid(), response, 100);
            return Result.operatorOk("注册成功");
        }
        return Result.operatorFail("注册失败");
    }

    /**
     * 登录
     * @return
     */
    @PostMapping(value = "/login",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result login(@Pattern(regexp = ValidatorRegx.EMAIL_REGX) @RequestParam("email") String email,
                        @RequestParam("password") String password,
                        HttpServletResponse response) {
        User user = userService.getUser(email, password);
        if (user == null) {
            return Result.operatorFail("邮箱或密码错误");
        }
        TokenUtils.createToken(user.getUid(), response, 100);
        return Result.operatorOk("登录成功");
    }

    @GetMapping("/isLogin")
    public Result check(HttpServletRequest request, HttpServletResponse response) {
        Long info = TokenUtils.getTokenInfo(request);
        if (info == null) {
            return Result.operatorFail("尚未登录");
        }
        User user = userService.getById(info);
        if (user == null) {
            TokenUtils.clearToken(response);
            return Result.operatorFail("尚未登录");
        }
        return Result.operatorOk("登陆中");
    }

    @GetMapping("/detail/{uid}")
    public Result detail(@PathVariable("uid") Long uid) {
        boolean admin = userService.isAdmin(uid);
        if (!admin) {
            return Result.operatorFail("非管理员用户");
        }
        AdminUser adminUser = adminUserService.getById(uid);
        UserDetailDto res = new UserDetailDto();
        if (adminUser != null) {
            BeanUtils.copyProperties(adminUser, res);
        }
        res.setUsername(userService.getUsername(uid));
        return Result.operatorOk("ok", res);
    }
}
