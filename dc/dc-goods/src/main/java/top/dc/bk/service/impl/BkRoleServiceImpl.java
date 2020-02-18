package top.dc.bk.service.impl;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.dc.bk.annotationn.RequiredLog;
import top.dc.bk.mapper.BkRoleMapper;
import top.dc.bk.mapper.BkRoleMenuMapper;
import top.dc.bk.mapper.BkUserRoleMapper;
import top.dc.bk.service.BkRoleService;
import top.dc.bk.utils.ShiroUtils;
import top.dc.bk.vo.BkRoleMenuVo;
import top.dc.pojo.bk.pojo.BkRole;
import top.dc.vo.PageVo;
import top.dc.vo.SysResult;

import java.util.Date;
import java.util.List;

@Service
public class BkRoleServiceImpl implements BkRoleService {

    @Autowired
    private BkRoleMapper bkRoleMapper;
    @Autowired
    private BkRoleMenuMapper bkRoleMenuMapper;
    @Autowired
    private BkUserRoleMapper bkUserRoleMapper;

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param roleName
     * @return
     */
    @RequiredLog("角色分页查询")
    @Override
    @RequiresPermissions("sys:role:list")
    public SysResult findRoleByPage(Integer currentPage, Integer pageSize, String roleName) {
        //验证参数合法性
        if(pageSize<=0) throw new IllegalArgumentException("请传入合法的分页大小");

        //查询总记录数
        Integer total = 0;
        try {
            total = bkRoleMapper.findRoleTotal(roleName);
        }catch (Exception e){
            throw new RuntimeException("数据库后台正在维护！");
        }
        //如果没有记录返回
        if(total==0) return SysResult.fail("当前没有记录");

        //参数校验和计算总页数
        Integer pageCount = (total-1)/pageSize + 1;
        if(pageCount<currentPage) throw new IllegalArgumentException("请传入合法的分页信息");

        //计算分页起始位置
        int startIndex = (currentPage-1)*pageSize;

        //进行分页查询
        List<BkRole> roleList = null;
        try {
            roleList = bkRoleMapper.findRoleByPage(roleName, startIndex, pageSize);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("数据库后台正在维护中，请稍候再试！");
        }

        //封装值对象
        PageVo<BkRole> pageVo = new PageVo<BkRole>().setCurrentPage(currentPage).setPageSize(pageSize).setTotal(total).setData(roleList);
        return SysResult.success(pageVo);
    }

    /**
     * 根据角色id查询对应的角色信息：
     *      角色基本信息
     *      角色对应的菜单信息
     * @param id
     * @return
     */
    @Override
    public SysResult doFindRoleById(Integer id) {
        if(id==null||id<1)
            throw new IllegalArgumentException("id值无效");
        BkRoleMenuVo roleMenuVo = bkRoleMapper.findRoleById(id);
        if(roleMenuVo == null)
            throw new RuntimeException("记录可能已经不存在");
        return SysResult.success("查询成功", roleMenuVo);
    }

    /**
     * 发现所有的角色信息
     * @return
     */
    @Override
    public SysResult doFindAllRole() {
        List<BkRole> bkRoleList = bkRoleMapper.selectList(null);
        if(bkRoleList==null)
            throw new RuntimeException("当前还未添加角色信息，请添加角色信息！");
        return SysResult.success(bkRoleList);
    }

    /**
     *根据角色id删除角色信息
     *  1.先删除关系表中对应角色的信息
     *      用户角色关系表
     *      菜单角色关系表
     *  2.删除对应的角色信息
     * @param id
     * @return
     */
    @Override
    @RequiredLog("删除角色")
    @RequiresPermissions("sys:role:delete")
    @Transactional
    public SysResult deleteRoleById(Integer id) {
        //1.根据Id删除用户-角色关系表中信息
        try {
            bkUserRoleMapper.deleteObjectsByRoleId(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("后台服务器异常！");
        }
        //2.删除角色-菜单关系表中的数据
        try {
            bkRoleMenuMapper.deleteObjectsByRoleId(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("后台服务器异常！");
        }
        //3.删除角色自身数据信息
        try {
            bkRoleMapper.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("后台服务器异常！");
        }
        return SysResult.success("删除成功！", null);
    }

    /**
     *新增角色信息：
     *  1.添加角色信息
     *  2.在关系表中添加角色和菜单的对应关系
     * @param entity
     * @return
     */
    @Override
    @RequiredLog("新增角色")
    @RequiresPermissions("sys:role:add")
    @Transactional
    public SysResult saveRole(BkRoleMenuVo entity) {
        BkRole role = new BkRole();
        //构造一个角色对象，
        role.setRoleName(entity.getRoleName()).setNote(entity.getNote())
                .setCreateUser(ShiroUtils.getUsername()).setModifiedUser(role.getCreateUser())
                .setCreateTime(new Date()).setUpdateTime(role.getCreateTime());
        int row = 0;
        //像角色表中添加角色数据
        try {
            row = bkRoleMapper.insert(role);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("后天服务器异常，请稍后尝试！");
        }
        Integer[] array = {};
        int rowRole = 0;
        //想角色菜单的关系表中跟新数据
        if(row != 0  && entity.getMenuIds()!=null && entity.getMenuIds().size()>0){
            try {
                rowRole = bkRoleMenuMapper.insertObjects(role.getId(), entity.getMenuIds().toArray(array));
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("后台服务器异常！");
            }
        }
        return SysResult.success("添加成功！", null);
    }

    /**
     *编辑角色信息
     *  跟新角色数据的信息
     *  跟新角色和菜单的对应关系
     * @param entity
     * @return
     */
    @Override
    @RequiredLog("编辑角色信息")
    @Transactional
    @RequiresPermissions("sys:role:edit")
    public SysResult editRoleById(BkRoleMenuVo entity) {
        BkRole role = new BkRole();
        //构造一个角色对象，
        role.setId(entity.getId()).setRoleName(entity.getRoleName()).setNote(entity.getNote())
                .setModifiedUser(ShiroUtils.getUsername())
                .setUpdateTime(new Date());
        int row = 0;
        //向角色表中更新角色数据
        try {
            row = bkRoleMapper.updateById(role);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("后台服务器异常，请稍后尝试！");
        }
        Integer[] array = {};
        //int rowRole = 0;
        //想角色菜单的关系表中跟新数据
        //  先删除原来的关系，在插入新的关系
        try {
            bkRoleMenuMapper.deleteObjectsByRoleId(role.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("后台服务器异常，请稍后尝试！");
        }
        if(row != 0 && entity.getMenuIds()!= null && entity.getMenuIds().size()!=0){
            try {
                bkRoleMenuMapper.insertObjects(role.getId(), entity.getMenuIds().toArray(array));
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("后台服务器异常！");
            }
        }
        return SysResult.success("修改成功！", null);

    }
}
