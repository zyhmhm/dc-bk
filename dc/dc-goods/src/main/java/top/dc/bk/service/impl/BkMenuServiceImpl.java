package top.dc.bk.service.impl;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.dc.bk.annotationn.RequiredLog;
import top.dc.bk.mapper.BkMenuMapper;
import top.dc.bk.mapper.BkRoleMenuMapper;
import top.dc.bk.service.BkMenuService;
import top.dc.pojo.bk.pojo.BkMenu;
import top.dc.vo.SysResult;

import java.util.Arrays;
import java.util.List;

@Service
public class BkMenuServiceImpl implements BkMenuService {

    @Autowired
    private BkMenuMapper bkMenuMapper;
    @Autowired
    private BkRoleMenuMapper bkRoleMenuMapper;

    @Override
    @RequiredLog("菜单查询")
    @RequiresPermissions("sys:menu_list")
    public SysResult findAllMenu() {
        List<BkMenu> bkMenus = bkMenuMapper.selectList(null);
        return SysResult.success(bkMenus);
    }

    /**
     * 根据Id删除菜单信息
     *      先删除角色-菜单关系表中的数据
     *      再删除对应的菜单数据
     * @param ids
     * @return
     */
    @Override
    @RequiredLog("删除菜单")
    @Transactional
    @RequiresPermissions("sys:menu:delete")
    public SysResult deleteMenuByIds(Integer[] ids) {
        //1.删除关系表中的数据
        for(Integer id: ids){
            try {
                bkRoleMenuMapper.deleteObjectsByMenuId(id);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("后台服务器异常");
            }
        }
        //2.删除角色菜单关系表中的数据
        List<Integer> idsList = Arrays.asList(ids);
        try {
            bkMenuMapper.deleteBatchIds(idsList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("后台服务器繁忙！");
        }
        return SysResult.success("删除成功！",null);
    }

    /**
     * 更新菜单数据
     * @param entity
     * @return
     */
    @Override
    @RequiredLog("编辑菜单")
    @RequiresPermissions("sys:menu:edit")
    @Transactional
    public SysResult updateMenu(BkMenu entity) {
        try {
            bkMenuMapper.updateById(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SysResult.success("更新成功！", null);
    }

    /**
     * 新增菜单数据
     * @param entity
     * @return
     */
    @Override
    @RequiredLog("新增商品")
    @RequiresPermissions("sys:menu:add")
    @Transactional
    public SysResult saveMenu(BkMenu entity) {
        try {
            bkMenuMapper.insert(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SysResult.success("添加成功！", null);
    }
}
