package top.dc.bk.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 此Dao对象负责操作bk_role_menus中间表数据
 */
@Mapper
@Repository
public interface BkRoleMenuMapper {

    /**
     * 保存角色菜单关系数据
     * @param roleId
     * @param menuIds
     * @return
     */
    int insertObjects(
            @Param("roleId")Integer roleId,
            @Param("menuIds")Integer[]menuIds);
    /**
     * 基于角色id删除菜单和角色的关系数据
     * @param : menuId
     * @return
     */
    @Delete("delete from bk_role_menus where role_id=#{roleId}")
    int deleteObjectsByRoleId(Integer roleId);
    /**
     * 基于菜单id删除菜单和角色的关系数据
     * @param menuId
     * @return
     */
    @Delete("delete from bk_role_menus where menu_id=#{menuId}")
    int deleteObjectsByMenuId(Integer menuId);

    /**
     * 根据觉得id查找对应的菜单id
     * @param roleIds
     * @return
     */
    List<Integer> findMenuIdsByRoleIds(
            @Param("roleIds")Integer[] roleIds);
}
