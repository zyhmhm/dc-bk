package top.dc.service.impl;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.dc.bk.annotationn.RequiredLog;
import top.dc.mapper.ShopMapper;
import top.dc.mapper.UserMapper;
import top.dc.pojo.Shop;
import top.dc.pojo.User;
import top.dc.service.ShopService;
import top.dc.vo.PageVo;
import top.dc.vo.SysResult;

import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 分页查询
     * @param shopName
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    @RequiredLog("对店铺进行分页查询")
    @RequiresPermissions("sys:shop:list")
    public SysResult findShopByPage(String shopName, Integer currentPage, Integer pageSize) {

        //验证参数合法性
        if(pageSize<=0) throw new IllegalArgumentException("请传入合法的分页大小");

        //查询总记录数
        Integer total = 0;
        try {
            total = shopMapper.findShopTotal(shopName);
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
        List<Shop> shopList = null;
        try {
            shopList = shopMapper.findShopByPage(shopName, startIndex, pageSize);
        }catch (Exception e){
            throw new RuntimeException("数据库后台正在维护中，请稍候再试！");
        }

        PageVo<Shop> pageVo = new PageVo<Shop>().setCurrentPage(currentPage).setPageSize(pageSize).setTotal(total).setData(shopList);
        return SysResult.success(pageVo);
    }

    /**
     * 根据id查询店铺的归属人
     * @param id
     * @return
     */
    @Override
    public String getUserNameById(Integer id) {
        User user = userMapper.selectById(id);
        if(user == null)
            throw new RuntimeException("没有该用户");
        return user.getUserName();
    }

    @Override
    @RequiredLog("改变店铺状态")
    @RequiresPermissions("sys:shop:ststue")
    public SysResult exchangeStatue(Shop shop) {


        try {

            shopMapper.updateById(shop);
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.fail("服务器繁忙，请稍候重试！");
        }
        return SysResult.success();
    }


}
