package top.wytbook.db;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName t_like
 */
@TableName(value ="t_like")
@Data
public class Like implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Long fromUid;

    /**
     * 
     */
    private Long targetId;

    /**
     * 0是文章点赞 1是普通点赞 2是回帖点赞
     */
    private Integer likeType;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}