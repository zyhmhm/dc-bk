package top.dc.bk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.dc.bk.service.BkDeptService;
import top.dc.pojo.bk.pojo.BkDept;
import top.dc.vo.SysResult;

@CrossOrigin
@RestController
@RequestMapping("/bkDept/")
public class BkDeptController {

    @Autowired
    private BkDeptService bkDeptService;

    /**
     * 对部门进行分页查询
     * @param currentPage
     * @param pageSize
     * @param deptName
     * @return
     */
    @GetMapping("findDeptByPage")
    public SysResult findDeptByPage(Integer currentPage, Integer pageSize, String deptName){
        return bkDeptService.findDeptByPage(currentPage, pageSize, deptName);
    }

    /**
     * 查询所有部门信息
     * @return
     */
    @GetMapping("findAllDept")
    public SysResult findAllDept(){
        return bkDeptService.findDeptAll();
    }
    /**
     * 根据部门Id查询部门的名称
     */
    @GetMapping("findDeptNameById")
    public SysResult findDeptNameById(Integer deptId){
        return bkDeptService.findDeptNameById(deptId);
    }

    /**
     * 增加部门信息
     */
    @PostMapping("saveDept")
    public SysResult saveDept(BkDept entity){
        return bkDeptService.saveDept(entity);
    }

    /**
     * 保存修改后的修改部门信息
     */
    @PostMapping("editDept")
    public SysResult editDept(BkDept entity){
        return bkDeptService.editDept(entity);
    }

    /**
     * 根据Id删除部门
     */
    @GetMapping("deleteDeptById")
    public SysResult deleteDeptById(Integer id){
        return bkDeptService.deleteDeptById(id);
    }

}
