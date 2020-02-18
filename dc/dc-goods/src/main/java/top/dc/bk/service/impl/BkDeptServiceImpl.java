package top.dc.bk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.dc.bk.annotationn.RequiredLog;
import top.dc.bk.mapper.BkDeptMapper;
import top.dc.bk.mapper.BkUserMapper;
import top.dc.bk.service.BkDeptService;
import top.dc.bk.utils.ShiroUtils;
import top.dc.pojo.bk.pojo.BkDept;
import top.dc.pojo.bk.pojo.BkUser;
import top.dc.vo.PageVo;
import top.dc.vo.SysResult;

import java.util.Date;
import java.util.List;

@Service
public class BkDeptServiceImpl implements BkDeptService {

    @Autowired
    private BkDeptMapper bkDeptMapper;
    @Autowired
    private BkUserMapper bkUserMapper;

    /**
     * 对部门进行分页查询
     * @param currentPage
     * @param pageSize
     * @param deptName
     * @return
     */
    @RequiresPermissions("sys:dept:list")
    @RequiredLog("部门分页查询")
    @Override
    public SysResult findDeptByPage(Integer currentPage, Integer pageSize, String deptName) {

        //验证参数合法性
        if(pageSize<=0) throw new IllegalArgumentException("请传入合法的分页大小");

        //查询总记录数
        Integer total = 0;
        try {
            total = bkDeptMapper.findDeptTotal(deptName);
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
        List<BkDept> deptList = null;
        try {
            deptList = bkDeptMapper.findDeptByPage(deptName, startIndex, pageSize);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("数据库后台正在维护中，请稍候再试！");
        }

        //封装值对象
        PageVo<BkDept> pageVo = new PageVo<BkDept>().setCurrentPage(currentPage).setPageSize(pageSize).setTotal(total).setData(deptList);
        return SysResult.success(pageVo);
    }

    /**
     * 获取所有得到部门信息
     * @return
     */
    @Override
    //@RequiresPermissions("sys:dept:list")
    public SysResult findDeptAll() {
        List<BkDept> bkDeptList = bkDeptMapper.selectList(null);
        if(bkDeptList==null)
            throw new RuntimeException("当前还未添加任何部门");
        return SysResult.success(bkDeptList);
    }

    @Override
    public SysResult findDeptNameById(Integer deptId) {
        BkDept bkDept = bkDeptMapper.selectById(deptId);
        if(bkDept==null)
            throw new RuntimeException("没有当前Id对应的部门");

        return SysResult.success(bkDept.getDeptName());
    }

    /**
     * 添加部门信息，并且添加增加该部门的管理员的信息和添加时间
     * @param entity
     * @return
     */
    @Override
    @RequiredLog("增加部门")
    @RequiresPermissions("sys:dept:add")
    public SysResult saveDept(BkDept entity) {
        entity.setCreateTime(new Date())
                .setUpdateTime(entity.getCreateTime());
        entity.setCreateUser(ShiroUtils.getUsername());
        entity.setModifiedUser(ShiroUtils.getUsername());
        int row = 0;
        try {
            row = bkDeptMapper.insert(entity);
        } catch (Exception e) {
            throw new RuntimeException("后台服务器繁忙！");
        }
        return SysResult.success("添加成功", null);
    }

    /**
     * 修改部门信息，并修改跟新时间和修改的用户
     * @param entity
     * @return
     */
    @Override
    @RequiredLog("编辑部门信息")
    @RequiresPermissions("sys:dept:edit")
    public SysResult editDept(BkDept entity) {
        entity.setUpdateTime(new Date());
        entity.setModifiedUser(ShiroUtils.getUsername());
        int row = 0;
        try {
            row = bkDeptMapper.updateById(entity);
        } catch (Exception e) {
            throw new RuntimeException("后台服务器繁忙！");
        }
        if(row == 0){
            return SysResult.fail("编辑失败！有可能该记录已经不存在");
        }
        return SysResult.success("修改成功",null);
    }

    /**
     * 根据Id删除对应的部门信息
     * @param id
     * @return
     */
    @Override
    @RequiredLog("删除部门")
    @RequiresPermissions("sys;dept:delete")
    public SysResult deleteDeptById(Integer id) {
        QueryWrapper<BkUser> query = new QueryWrapper<>();
        query.eq("dept_id", id);
        Integer rows = bkUserMapper.selectCount(query);
        if(rows >0)
            throw new RuntimeException("该部门下还有所属用户");
        int row = 0;
        try {
            row = bkDeptMapper.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("服务器繁忙！");
        }
        if(row == 0)
            return SysResult.fail("当前部门已经不存在！");
        return SysResult.success("删除成功", null);
    }
}
