package top.dc.bk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.dc.bk.service.BkLogsService;
import top.dc.vo.SysResult;

@RestController
@RequestMapping("/log")
@CrossOrigin
public class BkLogsController {
    @Autowired
    private BkLogsService bkLogsService;

    /**
     * 对日志进行分页查询
     * @param currentPage
     * @param pageSize
     * @param userName
     * @return
     */
    @GetMapping("/doFindObjects")
    public SysResult doFindObjects(Integer currentPage, Integer pageSize, String userName) {
        return bkLogsService.findGoodsByPage(currentPage, pageSize, userName);
    }

    /**
     * 删除选中的日志
     * @param ids
     * @return
     */
    @PostMapping("doDeleteLogsByIds")
    public SysResult doDeleteLogsByIds(Integer[] ids){
        return bkLogsService.doDeleteLogsByIds(ids);
    }

}
