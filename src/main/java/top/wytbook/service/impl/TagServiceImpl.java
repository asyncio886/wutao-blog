package top.wytbook.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import top.wytbook.constant.CacheStore;
import top.wytbook.db.Tag;
import top.wytbook.dto.PageDto;
import top.wytbook.service.TagService;
import top.wytbook.mapper.TagMapper;
import org.springframework.stereotype.Service;
import top.wytbook.util.ClearCacheAfterSave;

import java.io.Serializable;

/**
* @author lenovo
* @description 针对表【t_tag】的数据库操作Service实现
* @createDate 2023-01-07 18:07:22
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{
    @Autowired
    ClearCacheAfterSave clearCacheAfterSave;
    @Override
    @Cacheable(value = CacheStore.TAG_PREFIX, key = "'all'")
    public PageDto<Tag> getAllTags() {
        PageDto<Tag> res = new PageDto<>();
        res.setDataList(list());
        res.setSum(count());
        return res;
    }

    @Override
    @Cacheable(value = CacheStore.TAG_PREFIX, key = "#id")
    public Tag getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = CacheStore.TAG_PREFIX, key = "#entity.tid"),
                    @CacheEvict(value = CacheStore.TAG_PREFIX, key = "'all'")
            }
    )
    public boolean updateById(Tag entity) {
        return super.updateById(entity);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = CacheStore.TAG_PREFIX, key = "#id"),
                    @CacheEvict(value = CacheStore.TAG_PREFIX, key = "'all'")
            }
    )
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public boolean save(Tag entity) {
        boolean saved = super.save(entity);
        if (saved) {
            clearCacheAfterSave.remove(CacheStore.TAG_PREFIX, String.valueOf(entity.getTid()));
            clearCacheAfterSave.remove(CacheStore.TAG_PREFIX, "all");
        }
        return saved;
    }
}




