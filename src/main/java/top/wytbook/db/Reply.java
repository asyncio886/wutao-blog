package top.wytbook.db;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName t_reply
 */
@TableName(value ="t_reply")
@Data
public class Reply implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long rid;

    /**
     * 
     */
    private Long fromCid;

    /**
     * 
     */
    private String content;

    /**
     * 
     */
    private Long replyToUid;

    /**
     * 
     */
    private Long createTime;

    private Long authorUid;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}