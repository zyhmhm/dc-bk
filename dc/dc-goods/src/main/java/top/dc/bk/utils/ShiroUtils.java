package top.dc.bk.utils;

import org.apache.shiro.SecurityUtils;
import top.dc.pojo.bk.pojo.BkUser;

public class ShiroUtils {
    public static String getUsername() {
        return getUser().getUserName();
    }
    /**获取登录用户*/
    public static BkUser getUser() {
        return (BkUser)
                SecurityUtils.getSubject().getPrincipal();
    }
}
