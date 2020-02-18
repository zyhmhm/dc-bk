package top.dc.bk.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.dc.bk.service.BkUserService;
import top.dc.bk.utils.ShiroUtils;
import top.dc.pojo.bk.pojo.BkUser;
import top.dc.vo.SysResult;

@Controller
@RequestMapping("/bkUser")
@CrossOrigin
public class BkUserController {
    @Autowired
    private BkUserService bkUserService;

    /**
     * 登录
     */
    @PostMapping("/doLogin")
    @ResponseBody
    public SysResult doLogin(String username,String password){
        //1.获取Subject对象
        Subject subject= SecurityUtils.getSubject();
        //2.通过Subject提交用户信息,交给shiro框架进行认证操作
        //2.1对用户进行封装
        UsernamePasswordToken token=
                new UsernamePasswordToken(
                        username,//身份信息
                        password);//凭证信息
        //2.2对用户信息进行身份认证
        subject.login(token);
        //分析:
        //1)token会传给shiro的SecurityManager
        //2)SecurityManager将token传递给认证管理器
        //3)认证管理器会将token传递给realm
        return SysResult.success();
    }
    /**
     * 分页查询
     *     根据用户名分页查询
     */
    @GetMapping("/findUserByPage")
    @ResponseBody
    public SysResult findRoleByPage(Integer currentPage, Integer pageSize, String userName){
        return bkUserService.findUserByPage(currentPage, pageSize, userName);
    }

    /**
     * 根据用户id查询相应地角色信息
     */
    @GetMapping("/findRolesByUserId")
    @ResponseBody
    public SysResult findRolesByUserId(Integer userId){
        return bkUserService.findRolesByUserId(userId);
    }

    /**
     * 添加管理员
     *  1.添加管理员用户信息
     *  2.想关联表中更新数据（管理员用户-角色）
     */

    @PostMapping("saveUser")
    @ResponseBody
    public SysResult saveUser(BkUser entity,Integer[] role){
        return bkUserService.saveUser(entity,role);
    }

    @PostMapping("editBkUser")
    @ResponseBody
    public SysResult editBkUser(BkUser entity,Integer[] role){
        return bkUserService.editBkUser(entity,role);
    }

    /**
     * 改变管理员用户状态
     */
    @PostMapping("changeValid")
    @ResponseBody
    public SysResult changeValid(BkUser entity){
        return bkUserService.changeValid(entity);
    }

    /**
     * 返回当前登陆的用户名
     */
    @GetMapping("doGetLogUser")
    @ResponseBody
    public SysResult doGetLogUser(){
        return SysResult.success(ShiroUtils.getUsername());
    }
    /**
     * 修改管理员密码
     */
    @PostMapping("updatePassword")
    @ResponseBody
    public SysResult checkPassword(String password,String newPassword,String checkNewPassword){
        return bkUserService.updatePassword(password,newPassword,checkNewPassword);
    }
    /**
     * 根据用户名检查是否已经被使用
     */
    @GetMapping("checkUserName")
    @ResponseBody
    public SysResult checkUserName(String userName){
        return bkUserService.checkUserName(userName);
    }
}
