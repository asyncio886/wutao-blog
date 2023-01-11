package top.wytbook.db;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName t_article
 */
@TableName(value ="t_article")
@Data
public class Article implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long aid;

    /**
     * 
     */
    private String title;

    /**
     * 
     */
    private String content;

    /**
     * 
     */
    private Long createTime;

    /**
     * 
     */
    private Long modifiedTime;

    /**
     * 
     */
    private Integer likeCount;

    /**
     * 
     */
    private Integer watchCount;

    /**
     * 
     */
    private Long tagId;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private Long ownUid;

    private String facePicture;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}