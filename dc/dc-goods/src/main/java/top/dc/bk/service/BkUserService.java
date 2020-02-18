package top.dc.bk.service;

import top.dc.pojo.bk.pojo.BkUser;
import top.dc.vo.SysResult;

public interface BkUserService {
    SysResult findUserByPage(Integer currentPage, Integer pageSize, String userName);

    SysResult findRolesByUserId(Integer userId);

    SysResult saveUser(BkUser entity, Integer[] role);

    SysResult editBkUser(BkUser entity, Integer[] role);

    SysResult changeValid(BkUser entity);

    SysResult updatePassword(String password, String newPassword, String checkNewPassword);

    SysResult checkUserName(String userName);
}
