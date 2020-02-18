package top.dc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.dc.vo.SysResult;

@CrossOrigin
@Controller
public class UnAuthController {
    @RequestMapping("/unAuth")
    @ResponseBody
    public SysResult unAuth(){
        return SysResult.fail(101, "请重新登录");
    }
}
