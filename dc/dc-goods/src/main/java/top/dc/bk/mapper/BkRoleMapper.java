package top.dc.bk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.dc.bk.vo.BkRoleMenuVo;
import top.dc.pojo.bk.pojo.BkRole;

import java.util.List;

@Repository
public interface BkRoleMapper extends BaseMapper<BkRole> {
    Integer findRoleTotal(@Param("roleName") String roleName);

    List<BkRole> findRoleByPage(@Param("roleName") String roleName,@Param("startIndex") int startIndex,@Param("pageSize") Integer pageSize);

    BkRoleMenuVo findRoleById(@Param("id") Integer id);
}
