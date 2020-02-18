package top.dc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.dc.bk.annotationn.RequiredLog;
import top.dc.mapper.GenerMapper;
import top.dc.mapper.GoodsMapper;
import top.dc.mapper.ShopMapper;
import top.dc.pojo.Gener;
import top.dc.pojo.Goods;
import top.dc.pojo.Shop;
import top.dc.service.GoodsService;
import top.dc.vo.PageVo;
import top.dc.vo.SysResult;

import java.util.Date;
import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GenerMapper generMapper;
    @Autowired
    private ShopMapper shopMapper;


    /**
     * 对商品进行分页查询并按照更新时间排序
     *  1.  如果有用户名就按用户名分页查询
     *  2.  如果没有用户名就直接查询
     * @param currentPage 当前查询的页码
     * @param pageSize 分页的大小
     * @param goodsName 商品的名称
     * @return 系统返回值
     */
    @Override
    @RequiredLog("分页查询商品")
    @RequiresPermissions("sys:goods:list")
    public SysResult findGoodsByPage(Integer currentPage, Integer pageSize, String goodsName) {

        //验证参数合法性
        if(pageSize<=0) throw new IllegalArgumentException("请传入合法的分页大小");

        //查询总记录数
        Integer total = 0;
        try {
            total = goodsMapper.findGoodsTotal(goodsName);
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
        List<Goods> goodsList = null;
        try {
            goodsList = goodsMapper.findGoodsByPage(goodsName, startIndex, pageSize);
        }catch (Exception e){
            throw new RuntimeException("数据库后台正在维护中，请稍候再试！");
        }

        PageVo<Goods> pageVo = new PageVo<Goods>().setCurrentPage(currentPage).setPageSize(pageSize).setTotal(total).setData(goodsList);
        return SysResult.success(pageVo);
    }

    /**
     * 根据类别查询商品所属的类别
     *
     * @param generId 商品对应的类别id类别
     * @return 返回类别名称
     */
    @Override
    public String getGoodsGenerName(Integer generId) {
        Gener gener = generMapper.selectById(generId);
        return gener.getGenerName();
    }

    /**
     * 根据店铺id查询商品所属的店铺名称
     * @param shopId
     * @return
     */
    @Override
    public String getGoodsShopName(Integer shopId) {
        Shop shop = shopMapper.selectById(shopId);
        return shop.getShopName();
    }

    @Override
    @RequiredLog("改变商品状态")
    @RequiresPermissions("sys:goods:statue")
    public SysResult doChangeStatus( Goods goods) {

        if(goods==null || goods.getId()==null || goods.getStatue()==null)
            throw new RuntimeException("请传入合法的参数");
        goodsMapper.updateById(goods);
        return SysResult.success("修改成功！", null);
    }

    /**
     * 查询所有店铺的名称以及id，并返回
     * @return
     */
    @Override
    public SysResult findAllShopName() {
        List<Shop> shops = shopMapper.selectList(null);
        if(shops==null||shops.size()==0)
            return SysResult.fail("请先添加店铺！");
        return SysResult.success(shops);
    }

    /**
     * 根据店铺名称查询该店铺下面的所有类别
     * @param shopId
     * @return
     */

    @Override
    public SysResult doFindGenerByShopId(Integer shopId) {
        QueryWrapper<Gener> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shop_id", shopId);
        List<Gener> generList = generMapper.selectList(queryWrapper);

        if(generList==null || generList.size()==0)
            return SysResult.fail("请先为该店铺添加商品类别");
        return SysResult.success(generList);
    }

    /**
     * 添加商品
     * @param goods
     * @return
     */

    @Override
    @RequiredLog("增加商品")
    @RequiresPermissions("sys:goods:add")
    public SysResult addGoods(Goods goods) {
        goods.setStatue(1)
                .setNumber(0)
                .setFabulous(0)
                .setDiscount(0L)
                .setCreateTime(new Date())
                .setUpdateTime(goods.getCreateTime());
        goodsMapper.insert(goods);
        return SysResult.success("添加成功！", null);
    }

    /**
     * 跟新商品信息
     * @param goods
     * @return
     */
    @Override
    @RequiredLog("编辑商品")
    @RequiresPermissions("sys:goods:edit")
    public SysResult updateGoods(Goods goods) {

        goods.setUpdateTime(new Date());
        goodsMapper.updateById(goods);

        return SysResult.success("更新成功！",null);
    }

    /**
     *删除商品根据商品id
     * @param id
     * @return
     */

    @Override
    @RequiredLog("删除商品")
    @RequiresPermissions("sys:goods:delete")
    public SysResult deleteGoodsById(Integer id) {
        if(id==null || id<=0)
            throw new RuntimeException("请传入合法的id");
        int rows = goodsMapper.deleteById(id);
        if(rows==0)
            throw new RuntimeException("该记录已经不存在");
        return SysResult.success("删除成功", null);
    }
}
