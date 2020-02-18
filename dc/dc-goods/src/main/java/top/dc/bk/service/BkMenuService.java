package top.dc.bk.service;

import top.dc.pojo.bk.pojo.BkMenu;
import top.dc.vo.SysResult;

public interface BkMenuService {
    SysResult findAllMenu();

    SysResult deleteMenuByIds(Integer[] ids);

    SysResult updateMenu(BkMenu entity);

    SysResult saveMenu(BkMenu entity);
}
