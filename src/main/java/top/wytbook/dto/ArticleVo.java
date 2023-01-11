package top.wytbook.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ArticleVo {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    private String description;
    @NotNull
    private Long tagId;
    private String facePicture;
}
