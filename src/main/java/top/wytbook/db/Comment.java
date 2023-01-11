package top.wytbook.db;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName t_comment
 */
@TableName(value ="t_comment")
@Data
public class Comment implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long cid;

    /**
     * 
     */
    private String content;

    /**
     * 
     */
    private Long authorUid;

    /**
     * 
     */
    private Long createTime;

    /**
     * 
     */
    private Long fromAid;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}