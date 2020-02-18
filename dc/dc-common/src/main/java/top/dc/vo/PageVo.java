package top.dc.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PageVo<T> {
    private Integer currentPage; //当前页
    private Integer pageSize; //分页大小
    private Integer total;   //总条数
    private List<T> data; //返回的结果
}
