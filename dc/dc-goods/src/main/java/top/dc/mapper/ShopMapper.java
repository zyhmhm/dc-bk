package top.dc.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import top.dc.pojo.Shop;

import java.util.List;

@Repository
public interface ShopMapper extends BaseMapper<Shop> {

    Integer findShopTotal(String shopName);

    List<Shop> findShopByPage(String shopName, int startIndex, Integer pageSize);
}
