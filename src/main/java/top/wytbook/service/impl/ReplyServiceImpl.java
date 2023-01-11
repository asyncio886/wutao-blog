package top.wytbook.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.wytbook.db.Reply;
import top.wytbook.dto.PageDto;
import top.wytbook.dto.ReplyWithReplyToAndAuthor;
import top.wytbook.service.ReplyService;
import top.wytbook.mapper.ReplyMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author lenovo
* @description 针对表【t_reply】的数据库操作Service实现
* @createDate 2023-01-07 18:07:17
*/
@Service
public class ReplyServiceImpl extends ServiceImpl<ReplyMapper, Reply>
    implements ReplyService{
    @Override
    public Long getSumOfComment(Long cid) {
        LambdaQueryWrapper<Reply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Reply::getFromCid, cid);
        return count(queryWrapper);
    }

    @Override
    public PageDto<ReplyWithReplyToAndAuthor> getReplies(Long cid, Integer pn, Integer size) {
        PageDto<ReplyWithReplyToAndAuthor> res = new PageDto<>();
        List<ReplyWithReplyToAndAuthor> dataList = getBaseMapper().getRepliesByCid(cid, (pn - 1) * size, size);
        res.setSum(getSumOfComment(cid));
        res.setDataList(dataList);
        return res;
    }

    @Override
    public Long getFromCid(Long rid) {
        LambdaQueryWrapper<Reply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Reply::getFromCid).eq(Reply::getRid, rid);
        return getObj(queryWrapper, o -> (Long) o);
    }
}




