package top.dc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.dc.pojo.Shop;
import top.dc.service.ShopService;
import top.dc.vo.SysResult;

@RestController
@RequestMapping("/shop/")
@CrossOrigin
public class ShopController {
    @Autowired
    private ShopService shopService;

    /**
     * 分页查询
     * @param shopName 店铺名称
     * @param currentPage 当前页
     * @param pageSize 每页大小
     * @return
     */
    @RequestMapping("findShopByPage")
    public SysResult findShopByPage(String shopName,Integer currentPage,Integer pageSize){

        return shopService.findShopByPage(shopName,currentPage,pageSize);
    }
    /**
     * 根据id查询用户名，查询该店铺属于哪个用户
     */
    @RequestMapping("getUserNameById")
    public String getUserNameById(Integer id){
        return shopService.getUserNameById(id);
    }

    @PostMapping("exchangeStatue")
    public SysResult exchangeStatue(Shop shop){
        return shopService.exchangeStatue(shop);
    }

    /**
     *
     */
}
