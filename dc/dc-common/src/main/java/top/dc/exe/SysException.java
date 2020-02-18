package top.dc.exe;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.dc.vo.SysResult;

@RestControllerAdvice
@Slf4j
public class SysException {

    @ExceptionHandler(RuntimeException.class)
    public SysResult handleException(RuntimeException e){
        e.printStackTrace();
        log.error(e.getMessage());
        return SysResult.fail(e.getMessage());
    }
    @ExceptionHandler(ShiroException.class)
    public SysResult doHandleShiroException(
            ShiroException e) {
        SysResult r=new SysResult();
        r.setStatus(0);
        if(e instanceof UnknownAccountException) {
            r.setMsg("账户不存在");
        }else if(e instanceof LockedAccountException) {
            r.setMsg("账户已被禁用");
        }else if(e instanceof IncorrectCredentialsException) {
            r.setMsg("密码不正确");
        }else if(e instanceof AuthorizationException) {
            r.setMsg("没有此操作权限");
        }else {
            r.setMsg("系统维护中...");
        }
        e.printStackTrace();
        return r;
    }

}
