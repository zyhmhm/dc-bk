package top.dc.bk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.dc.bk.service.BkRoleService;
import top.dc.bk.vo.BkRoleMenuVo;
import top.dc.vo.SysResult;

@RestController
@CrossOrigin
@RequestMapping("/bkRole")
public class BkRoleController {
    @Autowired
    private BkRoleService bkRoleService;

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param roleName
     * @return
     */
    @GetMapping("/findRoleByPage")
    public SysResult findRoleByPage(Integer currentPage, Integer pageSize, String roleName){
        return bkRoleService.findRoleByPage(currentPage, pageSize, roleName);
    }

    /**
     * 根据角色id查询角色信息包括
     * @param id
     * @return
     */
    @GetMapping("/doFindRoleById")
    @ResponseBody
    public SysResult doFindRoleById(Integer id){
        return bkRoleService.doFindRoleById(id);
    }

    /**
     * 查询所有的的角色信息
     * @return
     */
    @GetMapping("/findAllRole")
    public SysResult findAllRole(){
        return bkRoleService.doFindAllRole();
    }

    /**
     *根据角色Id删除角色信息
     */
    @GetMapping("/deleteRoleById")
    public SysResult deleteRoleById(Integer id){
        return bkRoleService.deleteRoleById(id);
    }

    /**
     *新增角色信息
     */
    @PostMapping("/saveRole")
    public SysResult saveRole(BkRoleMenuVo entity){
        return bkRoleService.saveRole(entity);

    }

    /**
     *根据角色id修改角色信息
     */
    @PostMapping("/editRoleById")
    public SysResult editRoleById(BkRoleMenuVo entity){
        return bkRoleService.editRoleById(entity);
    }
}
