package top.wytbook.service;

import top.wytbook.db.User;
import com.baomidou.mybatisplus.extension.service.IService;
import top.wytbook.dto.PageDto;
import top.wytbook.dto.SafeUser;

import java.lang.reflect.InvocationTargetException;

/**
* @author lenovo
* @description 针对表【t_user】的数据库操作Service
* @createDate 2023-01-08 16:33:21
*/
public interface UserService extends IService<User> {

    boolean existByEmail(String email);

    boolean existByUsername(String username);

    User getUser(String email, String password);

    boolean isAdmin(Long uid);
    PageDto<SafeUser> getUserPage(Integer pn, Integer size, boolean needAdmin) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
    String getUsername(Long uid);
}
