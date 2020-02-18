package top.dc.bk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import top.dc.pojo.bk.pojo.BkDept;

import java.util.List;

@Repository
public interface BkDeptMapper extends BaseMapper<BkDept> {

    Integer findDeptTotal(String deptName);

    List<BkDept> findDeptByPage(String deptName, int startIndex, Integer pageSize);
}
