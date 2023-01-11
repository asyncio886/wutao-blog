package top.wytbook.db;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName t_admin_user
 */
@TableName(value ="t_admin_user")
@Data
public class AdminUser implements Serializable {
    /**
     * 
     */
    @TableId
    private Long uid;

    /**
     * 
     */
    private String avatar;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private String githubLink;

    /**
     * 
     */
    private String qqLink;

    /**
     * 
     */
    private String twitterLink;

    /**
     * 
     */
    private String weiboLink;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}