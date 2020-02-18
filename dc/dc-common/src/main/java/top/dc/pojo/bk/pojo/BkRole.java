package top.dc.pojo.bk.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.dc.pojo.BasePojo;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName(value = "bk_roles")
public class BkRole extends BasePojo {
    private static final long serialVersionUID = -4459507125257614060L;
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String roleName;
    private String note;
    private String createUser;
    private String modifiedUser;
}
