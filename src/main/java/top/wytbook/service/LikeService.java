package top.wytbook.service;

import top.wytbook.db.Like;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author lenovo
* @description 针对表【t_like】的数据库操作Service
* @createDate 2023-01-07 18:07:12
*/
public interface LikeService extends IService<Like> {
    boolean articleLike(Boolean st, Long fromUid, Long targetId);
    boolean hasLiked(Long fromUid, Long targetId);

    void addLikeRecord(Long aid, Long uid, Integer status);
}
