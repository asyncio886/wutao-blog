package top.wytbook.dto;

import lombok.Data;

import java.util.List;
@Data
public class PageDto <T>{
    List<T> dataList;
    Long sum;
}
