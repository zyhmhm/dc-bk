package top.dc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.dc.pojo.Gener;
import top.dc.vo.GenerPageVo;

import java.util.List;

@Repository
public interface GenerMapper extends BaseMapper<Gener> {
    List<GenerPageVo> getGenerByPage(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize);
}
