package top.dc.bk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import top.dc.bk.annotationn.RequiredLog;
import top.dc.bk.mapper.BkUserMapper;
import top.dc.bk.mapper.BkUserRoleMapper;
import top.dc.bk.service.BkUserService;
import top.dc.bk.utils.ShiroUtils;
import top.dc.pojo.bk.pojo.BkUser;
import top.dc.vo.PageVo;
import top.dc.vo.SysResult;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class BkUserServiceImpl implements BkUserService {
    @Autowired
    private BkUserMapper bkUserMapper;

    @Autowired
    private BkUserRoleMapper bkUserRoleMapper;

    @Override
    @RequiredLog("对管理员进行分页查询")
    @RequiresPermissions("sys:bkUser:list")
    public SysResult findUserByPage(Integer currentPage, Integer pageSize, String userName) {
//验证参数合法性
        if(pageSize<=0) throw new IllegalArgumentException("请传入合法的分页大小");

        //查询总记录数
        Integer total = 0;
        try {
            total = bkUserMapper.findUserTotal(userName);
        }catch (Exception e){
            throw new RuntimeException("数据库后台正在维护！");
        }
        //如果没有记录返回
        if(total==0) return SysResult.fail("当前没有记录");

        //参数校验和计算总页数
        Integer pageCount = (total-1)/pageSize + 1;
        if(pageCount<currentPage) throw new IllegalArgumentException("请传入合法的分页信息");

        //计算分页起始位置
        int startIndex = (currentPage-1)*pageSize;

        //进行分页查询
        List<BkUser> userList = null;
        try {
            userList = bkUserMapper.findUserByPage(userName, startIndex, pageSize);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("数据库后台正在维护中，请稍候再试！");
        }

        //封装值对象
        PageVo<BkUser> pageVo = new PageVo<BkUser>().setCurrentPage(currentPage).setPageSize(pageSize).setTotal(total).setData(userList);
        return SysResult.success(pageVo);
    }

    @Override
    public SysResult findRolesByUserId(Integer userId) {
        List<Integer> roleIds = bkUserRoleMapper.findRoleIdsByUserId(userId);
        return SysResult.success(roleIds);
    }

    /**
     * 新增用户
     *  1.像表中插入新增用户信息，并对密码进行加密
     *  2.想用户-角色的关系表中插入数据
     * @param entity
     * @param role
     * @return
     */
    @Override
    @RequiredLog("增加管理员")
    @RequiresPermissions("sys:bkUser:add")
    @Transactional
    public SysResult saveUser(BkUser entity, Integer[] role) {
        //1.验证数据合法性
        if(entity==null)
            throw new RuntimeException("保存对象不能为空");
        if(StringUtils.isEmpty(entity.getUserName()))
            throw new RuntimeException("用户名不能为空");
        if(StringUtils.isEmpty(entity.getPassword()))
            throw new RuntimeException("密码不能为空");
        if(role==null || role.length==0)
            throw new RuntimeException("至少要为用户分配角色");
        //对用户密码进行加密
        String password = entity.getPassword();
        String slat = UUID.randomUUID().toString();
        //设置用户其他属性
        SimpleHash sHash = new SimpleHash("MD5", password, slat, 1);
        entity.setPassword(sHash.toHex()).setSalt(slat).setCreateUser(ShiroUtils.getUsername()).setValid(1)
                .setModifiedUser(ShiroUtils.getUsername()).setCreateTime(new Date()).setUpdateTime(entity.getCreateTime());
        //将用户信息存入到数据库
        bkUserMapper.insert(entity);

        //更新角色-用户关系表数据
        bkUserRoleMapper.insertObjects(entity.getId(), role);

        return SysResult.success("创建成功！", null);
    }

    /**
     * 更新管理员用户
     * @param entity
     * @param role
     * @return
     */
    @Override
    @RequiredLog("编辑管理员")
    @RequiresPermissions("sys:bkUser:edit")
    public SysResult editBkUser(BkUser entity, Integer[] role) {
        //1.验证数据合法性
        if(entity==null)
            throw new RuntimeException("保存对象不能为空");
        if(StringUtils.isEmpty(entity.getUserName()))
            throw new RuntimeException("用户名不能为空");
        if(StringUtils.isEmpty(entity.getPassword()))
            throw new RuntimeException("密码不能为空");
        if(role==null || role.length==0)
            throw new RuntimeException("至少要为用户分配角色");
        //2.将更新后的数据存入到数据库表
        BkUser bkUser = new BkUser();
        bkUser.setId(entity.getId()).setUserName(entity.getUserName())
                .setDeptId(entity.getDeptId()).setMobile(entity.getMobile())
                .setEmail(entity.getEmail()).setUpdateTime(new Date());
        bkUser.setModifiedUser(ShiroUtils.getUsername());

        bkUserMapper.updateById(bkUser);
        bkUserRoleMapper.deleteObjectsByUserId(entity.getId());

        bkUserRoleMapper.insertObjects(entity.getId(), role);
        return SysResult.success("更新陈功！", null);
    }

    /**
     * 改变管理员用户的状态
     * @param entity
     * @return
     */
    @Override
    @RequiredLog("改变管理员状态")
    @RequiresPermissions("sys:bkUser:valid")
    public SysResult changeValid(BkUser entity) {
        BkUser bkUser = new BkUser();
        bkUser.setId(entity.getId()).setValid(entity.getValid()).setUpdateTime(new Date());
        bkUser.setModifiedUser(ShiroUtils.getUsername());

        try {
            bkUserMapper.updateById(bkUser);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return SysResult.success("状态改变成功！", null);
    }

    /**
     * 修改密码
     * @param password
     * @param newPassword
     * @param checkNewPassword
     * @return
     */
    @Override
    public SysResult updatePassword(String password, String newPassword, String checkNewPassword) {
        //1.判定新密码与密码确认是否相同
        if(StringUtils.isEmpty(newPassword))
            throw new IllegalArgumentException("新密码不能为空");
        if(StringUtils.isEmpty(checkNewPassword))
            throw new IllegalArgumentException("确认密码不能为空");
        if(!newPassword.equals(checkNewPassword))
            throw new IllegalArgumentException("两次输入的密码不相等");
        //2.判定原密码是否正确
        if(StringUtils.isEmpty(password))
            throw new IllegalArgumentException("原密码不能为空");
        //获取登陆用户
        BkUser user=(BkUser) SecurityUtils.getSubject().getPrincipal();
        SimpleHash sh=new SimpleHash("MD5",
                password, user.getSalt(), 1);
        if(!user.getPassword().equals(sh.toHex()))
            throw new IllegalArgumentException("原密码不正确");
        //3.对新密码进行加密
        String salt = UUID.randomUUID().toString();
        sh = new SimpleHash("MD5",newPassword,salt,1);
        user.setPassword(sh.toHex()).setSalt(salt).setUpdateTime(new Date());
        QueryWrapper<BkUser> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", user.getUserName());
        try {
            int row = bkUserMapper.update(user, wrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("修改密码失败！");
        }
        //退出操作
        SecurityUtils.getSubject().logout();
        return SysResult.success("修改成功", null);
    }

    @Override
    public SysResult checkUserName(String userName) {
        QueryWrapper<BkUser> query = new QueryWrapper<>();
        query.eq("user_name", userName);
        BkUser bkUser = null;
        try {
            bkUser = bkUserMapper.selectOne(query);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("后台服务器出现异常！");
        }
        if(bkUser==null){
            return SysResult.success();
        }
        return SysResult.fail(102, "该用户名已经被使用");
    }


}
