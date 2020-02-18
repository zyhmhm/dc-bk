package top.dc.bk.service;

import top.dc.bk.vo.BkRoleMenuVo;
import top.dc.vo.SysResult;

public interface BkRoleService {
    SysResult findRoleByPage(Integer currentPage, Integer pageSize, String roleName);

    SysResult doFindRoleById(Integer id);

    SysResult doFindAllRole();

    SysResult deleteRoleById(Integer id);

    SysResult saveRole(BkRoleMenuVo entity);

    SysResult editRoleById(BkRoleMenuVo entity);
}
