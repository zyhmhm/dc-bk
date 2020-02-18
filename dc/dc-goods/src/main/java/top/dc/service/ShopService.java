package top.dc.service;

import top.dc.pojo.Shop;
import top.dc.vo.SysResult;

public interface ShopService {
    SysResult findShopByPage(String shopName, Integer currentPage, Integer pageSize);

    String getUserNameById(Integer id);

    SysResult exchangeStatue(Shop shop);
}
