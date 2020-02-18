package top.dc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import top.dc.pojo.Goods;

import java.util.List;

@Repository
public interface GoodsMapper extends BaseMapper<Goods> {

    List<Goods> findGoodsByPage(String goodsName, int startIndex, Integer pageSize);
    Integer findGoodsTotal(String goodsName);
}
