package top.dc.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("t_gener")
public class Gener extends BasePojo {
    private static final long serialVersionUID = 3486024664299912472L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String generName;
    private Integer shopId;

}
