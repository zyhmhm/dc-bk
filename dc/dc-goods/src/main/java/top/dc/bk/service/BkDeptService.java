package top.dc.bk.service;

import top.dc.pojo.bk.pojo.BkDept;
import top.dc.vo.SysResult;

public interface BkDeptService {
    SysResult findDeptByPage(Integer currentPage, Integer pageSize, String userName);

    SysResult findDeptAll();

    SysResult findDeptNameById(Integer deptId);

    SysResult saveDept(BkDept entity);

    SysResult editDept(BkDept entity);

    SysResult deleteDeptById(Integer id);
}
