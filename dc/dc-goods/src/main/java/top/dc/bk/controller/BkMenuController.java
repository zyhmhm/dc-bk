package top.dc.bk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.dc.bk.service.BkMenuService;
import top.dc.pojo.bk.pojo.BkMenu;
import top.dc.vo.SysResult;

@RestController
@RequestMapping("/bkMenu/")
@CrossOrigin
public class BkMenuController {
    @Autowired
    private BkMenuService bkMenuService;

    @GetMapping("findAllMenu")
    public SysResult findAllMenu(){
        return bkMenuService.findAllMenu();
    }

    /**
     * 根据id删除对应的菜单
     */
    @PostMapping("deleteMenuByIds")
    public SysResult deleteMenuByIds(Integer[] ids){
        return bkMenuService.deleteMenuByIds(ids);
    }

    /**
     * 更新菜单信息
     */
    @PostMapping("updateMenuById")
    public SysResult updateMenu(BkMenu entity){
        return bkMenuService.updateMenu(entity);
    }
    /**
     * 新增菜单
     */
    @PostMapping("saveMenu")
    public SysResult saveMenu(BkMenu entity){
        return bkMenuService.saveMenu(entity);
    }
}
