package top.wytbook.db;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName t_tag
 */
@TableName(value ="t_tag")
@Data
public class Tag implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long tid;

    /**
     * 
     */
    private String tagName;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private String tagRoute;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}