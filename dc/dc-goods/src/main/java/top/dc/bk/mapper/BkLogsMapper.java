package top.dc.bk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.dc.pojo.bk.pojo.BkLogs;

import java.util.List;

@Repository
public interface BkLogsMapper extends BaseMapper<BkLogs> {

    //查询分页的信息
    List<BkLogs> findLogsByPage(@Param("userName") String userName, @Param("startIndex") int startIndex, @Param("pageSize") Integer pageSize);

    //根据用户名查询相应的记录总条数
    Integer findLogsTotal(@Param("userName") String userName);
}
