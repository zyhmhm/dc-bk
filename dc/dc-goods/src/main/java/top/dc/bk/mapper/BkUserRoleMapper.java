package top.dc.bk.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BkUserRoleMapper {
    /**
     * 基于用户id查询对应的角色ID
     * @param id
     * @return
     */
    @Select("select role_id from bk_user_roles where user_id=#{userId}")
    List<Integer> findRoleIdsByUserId(Integer id);
    /**
     * 将用户和角色的关系数据存储到数据库
     * @param userId
     * @param roleIds
     * @return
     */
    int insertObjects(
            @Param("userId")Integer userId,
            @Param("roleIds")Integer[]roleIds);
    /**
     * 基于用户id删除用户与角色的关系数据
     * @param userId
     * @return
     */
    @Delete("delete from bk_user_roles where user_id=#{userId}")
    int deleteObjectsByUserId(Integer userId);
    /**
     * 基于角色id删除用户与角色的关系数据
     * @param roleId
     * @return
     */
    @Delete("delete from bk_user_roles where role_id=#{roleId}")
    int deleteObjectsByRoleId(Integer roleId);

}
