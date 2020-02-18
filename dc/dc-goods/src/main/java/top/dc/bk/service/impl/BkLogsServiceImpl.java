package top.dc.bk.service.impl;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.dc.bk.annotationn.RequiredLog;
import top.dc.bk.mapper.BkLogsMapper;
import top.dc.bk.service.BkLogsService;
import top.dc.pojo.bk.pojo.BkLogs;
import top.dc.vo.PageVo;
import top.dc.vo.SysResult;

import java.util.Arrays;
import java.util.List;

@Service
public class BkLogsServiceImpl implements BkLogsService {

    @Autowired
    private BkLogsMapper bkLogsMapper;


    /**
     * 对日志进行分页查询，根据用户名查询，并根据创建时间将序排列
     * @param currentPage 当前页
     * @param pageSize 页面大小
     * @param userName 用户名
     * @return
     */
    @Override
    @RequiredLog("商品分页查询")
    @RequiresPermissions("sys:log:list")
    public SysResult findGoodsByPage(Integer currentPage, Integer pageSize, String userName) {
        //验证参数合法性
        if(pageSize<=0) throw new IllegalArgumentException("请传入合法的分页大小");

        //查询总记录数
        Integer total = 0;
        try {
            total = bkLogsMapper.findLogsTotal(userName);
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
        List<BkLogs> logList = null;
        try {
            logList = bkLogsMapper.findLogsByPage(userName, startIndex, pageSize);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("数据库后台正在维护中，请稍候再试！");
        }

        PageVo<BkLogs> pageVo = new PageVo<BkLogs>().setCurrentPage(currentPage).setPageSize(pageSize).setTotal(total).setData(logList);
        return SysResult.success(pageVo);
    }

    /**
     * 根据Ids删除选中的日志信息
     * @param ids
     * @return
     */
    @Override
    @RequiredLog("删除日志信息")
    @RequiresPermissions("sys:log:delete")
    public SysResult doDeleteLogsByIds(Integer[] ids) {

        List<Integer> idsList = Arrays.asList(ids);
        bkLogsMapper.deleteBatchIds(idsList);
        return SysResult.success("删除成功！", null);
    }

    @Override
    public void saveObject(BkLogs bkLog) {
        bkLogsMapper.insert(bkLog);
    }
}
