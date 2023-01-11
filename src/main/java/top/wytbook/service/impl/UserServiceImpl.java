package top.wytbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import top.wytbook.constant.CacheStore;
import top.wytbook.constant.UserType;
import top.wytbook.db.User;
import top.wytbook.dto.PageDto;
import top.wytbook.dto.SafeUser;
import top.wytbook.service.UserService;
import top.wytbook.mapper.UserMapper;
import org.springframework.stereotype.Service;
import top.wytbook.util.DtoUtils;
import top.wytbook.util.Md5Utils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
* @author lenovo
* @description 针对表【t_user】的数据库操作Service实现
* @createDate 2023-01-08 16:33:21
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Override
    public boolean existByEmail(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        return getBaseMapper().exists(queryWrapper);
    }

    @Override
    public boolean existByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        return getBaseMapper().exists(queryWrapper);
    }

    @Override
    public User getUser(String email, String password) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        User user = getOne(queryWrapper);
        if (user == null) {
            return null;
        }
        String encodedPassword = Md5Utils.getMd5String(password, user.getSalt());
        if (user.getPassword().equals(encodedPassword)) {
            return user;
        }
        return null;
    }

    @Override
    public boolean isAdmin(Long uid) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(User::getUserType).eq(User::getUid, uid);
        Integer userType = getObj(queryWrapper, o -> (Integer) o);
        if (userType == null) {
            return false;
        }
        return userType.equals(UserType.ADMIN);
    }

    @Override
    public PageDto<SafeUser> getUserPage(Integer pn, Integer size, boolean needAdmin) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Page<User> userPage = new Page<>(pn, size, true);
        PageDto<SafeUser> res = new PageDto<>();
        if (needAdmin) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUserType, UserType.ADMIN);
            page(userPage, queryWrapper);
        }
        else {
            page(userPage);
        }
        List<User> records = userPage.getRecords();
        res.setDataList(DtoUtils.convertList(records, SafeUser.class));
        res.setSum(userPage.getTotal());
        return res;
    }

    @Override
    @Cacheable(value = CacheStore.USER_PREFIX, key = "#id")
    public User getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    @CacheEvict(value = CacheStore.USER_PREFIX, key = "#entity.uid")
    public boolean updateById(User entity) {
        return super.updateById(entity);
    }

    @Override
    public String getUsername(Long uid) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(User::getUsername).eq(User::getUid, uid);
        return getObj(queryWrapper, o-> (String) o);
    }
}




