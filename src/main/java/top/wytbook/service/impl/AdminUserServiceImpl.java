package top.wytbook.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import top.wytbook.constant.CacheStore;
import top.wytbook.db.AdminUser;
import top.wytbook.service.AdminUserService;
import top.wytbook.mapper.AdminUserMapper;
import org.springframework.stereotype.Service;
import top.wytbook.util.ClearCacheAfterSave;

import java.io.Serializable;

/**
* @author lenovo
* @description 针对表【t_admin_user】的数据库操作Service实现
* @createDate 2023-01-08 22:18:51
*/
@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser>
    implements AdminUserService{
    @Autowired
    ClearCacheAfterSave clearCacheAfterSave;
    @Override
    @Cacheable(value = CacheStore.ADMIN_USER_PREFIX, key = "#id")
    public AdminUser getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public boolean save(AdminUser entity) {
        boolean saved = super.save(entity);
        if (saved) {
            clearCacheAfterSave.remove(CacheStore.ADMIN_USER_PREFIX, String.valueOf(entity.getUid()));
        }
        return saved;
    }

    @Override
    @CacheEvict(value = CacheStore.ADMIN_USER_PREFIX, key = "#entity.uid")
    public boolean updateById(AdminUser entity) {
        return super.updateById(entity);
    }
}




