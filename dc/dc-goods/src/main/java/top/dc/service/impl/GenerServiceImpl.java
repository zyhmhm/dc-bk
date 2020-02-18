package top.dc.service.impl;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.dc.bk.annotationn.RequiredLog;
import top.dc.mapper.GenerMapper;
import top.dc.pojo.Gener;
import top.dc.service.GenerService;
import top.dc.vo.GenerPageVo;
import top.dc.vo.PageVo;
import top.dc.vo.SysResult;

import java.util.List;

@Service
public class GenerServiceImpl implements GenerService {

    @Autowired
    private GenerMapper generMapper;

    /**
     * 分页查询商品类别信息
     * @param currentPage
     * @return
     */
    @Override
    @RequiredLog("对类别进行分页查询")
    @RequiresPermissions("sys:gener:list")
    public SysResult findGenerByPage(Integer currentPage) {
        //参数验证
        int rowcount = generMapper.selectCount(null);
        if(rowcount<=0) return SysResult.fail("暂时还没有商品类别");
        //计算分页信息
        int pageSize = 10;
        int pageCount = (rowcount-1)/pageSize+1;
        if(currentPage>pageCount || currentPage<1) throw new IllegalArgumentException("请传入合法的页参数");
        int startIndex = (currentPage-1)*pageSize;
        List<GenerPageVo> generList = generMapper.getGenerByPage(startIndex, pageSize);
        PageVo<GenerPageVo> pageVo = new PageVo<>();
        pageVo.setCurrentPage(currentPage).setPageSize(pageSize).setTotal(rowcount).setData(generList);
        return SysResult.success(pageVo);
    }

    @Override
    @RequiredLog("更新类别名称")
    @RequiresPermissions("sys:gener:edit")
    public SysResult updateGenerNameById(Gener entity) {

        if(StringUtils.isEmpty(entity.getGenerName()))
            return SysResult.fail("请输入有效的名字");
        String str = entity.getGenerName().trim();
        if(str.length()==0)
            return SysResult.fail("输入的名字不能全为空格");
        if(entity.getGenerName().length()>20 || entity.getGenerName().length()<2)
            return SysResult.fail("请输入2-20个字的名字");
        generMapper.updateById(entity);

        return SysResult.success();
    }

 /*   @Override
    public SysResult deleteGenerById(Integer id) {

        return null;
    }*/
}
