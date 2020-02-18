package top.dc.bk.service.realm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import top.dc.bk.mapper.BkMenuMapper;
import top.dc.bk.mapper.BkRoleMenuMapper;
import top.dc.bk.mapper.BkUserMapper;
import top.dc.bk.mapper.BkUserRoleMapper;
import top.dc.pojo.bk.pojo.BkUser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ShiroUserRealm extends AuthorizingRealm {
    @Autowired
    private BkUserMapper bkUserMapper;
    @Autowired
    private BkUserRoleMapper bkUserRoleMapper;
    @Autowired
    private BkRoleMenuMapper bkRoleMenuMapper;
    @Autowired
    private BkMenuMapper bkMenuMapper;

    /**
     * 设置凭证匹配器(与用户添加操作使用相同的加密算法)
     */
    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        //构建凭证对象
        HashedCredentialsMatcher cMatcher = new HashedCredentialsMatcher();
        //设置加密算法
        cMatcher.setHashAlgorithmName("MD5");
        //设置加密次数
        cMatcher.setHashIterations(1);
        super.setCredentialsMatcher(cMatcher);
    }

    /**
     * 通过此方法完成认证数据的获取及封装,系统
     * 底层会将认证数据传递认证管理器，由认证
     * 管理器完成登录认证操作。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1.获取用户名（用户页面输入）
        UsernamePasswordToken upToken =
                (UsernamePasswordToken) token;
        String userName = upToken.getUsername();
        //2.基于用户名查询用户信息
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_name", userName);
        BkUser bkUser = bkUserMapper.selectOne(queryWrapper);
        //判断用户是否为空（）存在
        if(bkUser==null)
            throw new UnknownAccountException();
        //判断用户是否已经被禁用
        if(bkUser.getValid()==0)
            throw  new LockedAccountException();
        //封装用户信息
        ByteSource credentialsSalt = ByteSource.Util.bytes(bkUser.getSalt());
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(bkUser,//principal (身份)
                bkUser.getPassword(),//hashedCredentials
                credentialsSalt, //credentialsSalt
                getName());
        return info;
    }

    /**
     * 该部分是实现权限管理的一部分
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo
    (PrincipalCollection principals) {
        //1.获取登录用户信息，例如用户Id
        BkUser user = (BkUser) principals.getPrimaryPrincipal();
        Integer userId = user.getId();
        //2.基于用户id获取用户拥有的角色
        List<Integer> roleIds = bkUserRoleMapper.findRoleIdsByUserId(userId);
        if(roleIds == null || roleIds.size()==0)
            throw new AuthorizationException();
        //3.基于角色id获取用户所用的的的菜单id
        Integer[] array = {};
        array = roleIds.toArray(array);
        List<Integer> menuIds = bkRoleMenuMapper.findMenuIdsByRoleIds(array);
        if(menuIds == null || menuIds.size()==0)
            throw new AuthorizationException();
        //4.基于用户所拥有的菜单的id获取用户的所有权限
        List<String> permissions=
                bkMenuMapper.findPermissions(
                        menuIds.toArray(array));

        //5.对权限标识信息进行封装并返回
        Set<String> set=new HashSet<>();
        for(String per:permissions){
            if(!StringUtils.isEmpty(per)){
                set.add(per);
            }
        }
        SimpleAuthorizationInfo info=
                new SimpleAuthorizationInfo();
        info.setStringPermissions(set);
        return info;//返回给授权管理器
    }

}
