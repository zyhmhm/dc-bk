package top.dc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.dc.pojo.Gener;
import top.dc.service.GenerService;
import top.dc.vo.SysResult;

@CrossOrigin
@RestController
@RequestMapping("/gener/")
public class GenerController {

    @Autowired
    private GenerService generService;

    @RequestMapping("findGenerByPage")
    public SysResult findGenerByPage(Integer currentPage){

        return generService.findGenerByPage(currentPage);
    }

    @RequestMapping("updateGenerNameById")
    public SysResult updateGenerNameById(Gener gener){

        return generService.updateGenerNameById(gener);
    }

    /*@PostMapping("deleteGenerById")
    public SysResult deleteGenerById(Integer id){
        return generService.deleteGenerById(id);
    }*/

}
