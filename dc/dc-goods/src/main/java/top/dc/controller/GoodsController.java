package top.dc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.dc.pojo.Goods;
import top.dc.service.GoodsService;
import top.dc.vo.SysResult;

@RestController
@RequestMapping("/goods/")
@CrossOrigin
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    /**
     * 查询商品
     * @param currentPage
     * @param pageSize
     * @param goodsName
     * @return
     */
    @GetMapping("findGoodsByPage")
    public SysResult findGoodsByPage(Integer currentPage, Integer pageSize, String goodsName) {
        return goodsService.findGoodsByPage(currentPage, pageSize, goodsName);
    }

    /**
     * 查询商品的类别名称
     * @param generId
     * @return
     */
    @GetMapping("doGetGoodsGenerName")
    public String doGetGoodsGenerName(Integer generId) {

        return goodsService.getGoodsGenerName(generId);
    }

    /**
     * 查询商品所属店铺
     * @param shopId
     * @return
     */
    @GetMapping("doGetGoodsShopName")
    public String doGetGoodsShopName(Integer shopId){

        return goodsService.getGoodsShopName(shopId);
    }

    /**
     * 改变商品状态
     */
    @PostMapping("doChangeStatus")
    public SysResult doChangeStatus(Goods goods){

        return goodsService.doChangeStatus(goods);
    }

    /**
     * 新增商品时查询所有店铺的名称和id
     */
    @GetMapping("shop/findAllShopName")
    @ResponseBody
    public SysResult findAllShopName(){

        return SysResult.success(goodsService.findAllShopName());
    }

    /**
     *根据选择的店铺id查询相应的类别数据
     */
    @GetMapping("gener/doFindGenerByShopId")
    @ResponseBody
    public SysResult doFindGenerByShopId(Integer shopId){

        return SysResult.success(goodsService.doFindGenerByShopId(shopId));
    }

    /**
     * 新增商品
     */
    @RequestMapping("addGoods")
    @ResponseBody
    public SysResult addGoods(Goods goods){

        return goodsService.addGoods(goods);
    }

    /**
     * 更新商品信息
     */
    @PostMapping("updateGoods")
    @ResponseBody
    public SysResult updateGoods(Goods goods){
        return goodsService.updateGoods(goods);
    }

    /**
     * 根据商品id删除商品
     */
    @PostMapping("deleteGoodsById")
    @ResponseBody
    public SysResult deleteGoodsById(Integer id){

        return goodsService.deleteGoodsById(id);
    }

}