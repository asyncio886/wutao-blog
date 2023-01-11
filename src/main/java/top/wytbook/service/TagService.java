package top.wytbook.service;

import top.wytbook.db.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import top.wytbook.dto.PageDto;

/**
* @author lenovo
* @description 针对表【t_tag】的数据库操作Service
* @createDate 2023-01-07 18:07:22
*/
public interface TagService extends IService<Tag> {
    PageDto<Tag> getAllTags();
}
