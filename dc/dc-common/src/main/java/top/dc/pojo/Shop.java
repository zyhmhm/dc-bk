package top.dc.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_shop")
public class Shop extends BasePojo{
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String shopName;
    private String addr;
    private String tell;
    private Integer userId;
    private Integer statue;
    private String openTime;
    private String closeTime;
    private String imageUrl;
    private String des;


}
