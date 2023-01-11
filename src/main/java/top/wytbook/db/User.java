package top.wytbook.db;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName t_user
 */
@TableName(value ="t_user")
@Data
public class User implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long uid;

    /**
     * 
     */
    private String email;

    /**
     * 
     */
    private String username;

    /**
     * 
     */
    private Long createTime;

    /**
     * 用户类型 0为普通 1为管理者
     */
    private Integer userType;

    /**
     * 
     */
    private String password;

    /**
     * 
     */
    private String salt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}