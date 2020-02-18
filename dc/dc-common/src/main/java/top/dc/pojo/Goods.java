package top.dc.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Accessors(chain = true)
@TableName("t_goods")
public class Goods extends BasePojo {

    private static final long serialVersionUID = 8280948940281824471L;

    @TableId(type = IdType.AUTO)
    private Integer id;         //主键
    private String goodsName;    //商品名称
    private Long price;         //商品价格  扩大10000之后的结果
    private String des;         //商品描述
    private String imageUrl;    //商品图片
    private Integer statue;     //商品状态
    private Integer fabulous;   //商品点赞数
    private Integer number;     //商品销售数量
    private Long discount;      //商品则扣
    private Integer shopId;     //商品所属店铺id
    private Integer generId;    //商品类别id

   public String[] getImageUrls(){
       if(StringUtils.isEmpty(imageUrl))
           return null;
        return imageUrl.split(",");
    }

}
