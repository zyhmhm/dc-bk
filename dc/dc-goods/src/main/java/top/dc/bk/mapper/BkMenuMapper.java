package top.dc.bk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.dc.pojo.bk.pojo.BkMenu;

import java.util.List;

@Repository
public interface BkMenuMapper extends BaseMapper<BkMenu> {

    /**
     * 根据菜单id查询对应的权限标识
     * @param menuIds
     * @return
     */
    List<String> findPermissions(@Param("menuIds") Integer[] menuIds);

}
