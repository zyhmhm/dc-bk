package top.dc.service;

import top.dc.pojo.Goods;
import top.dc.vo.SysResult;

public interface GoodsService {

    SysResult findGoodsByPage(Integer currentPage, Integer pageSize, String goodsName);

    String getGoodsGenerName(Integer generId);

    String getGoodsShopName(Integer shopId);

    SysResult doChangeStatus(Goods goods);

    SysResult findAllShopName();

    SysResult doFindGenerByShopId(Integer shopId);

    SysResult addGoods(Goods goods);

    SysResult updateGoods(Goods goods);

    SysResult deleteGoodsById(Integer id);
}
