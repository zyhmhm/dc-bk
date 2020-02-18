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
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "bk_users")
public class BkUser extends BasePojo {
    private static final long serialVersionUID = -18185876341566688L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String userName;
    private String password;
    private String salt;
    private String email;
    private String mobile;
    private Integer valid;
    private Integer deptId;
    private String createUser;
    private String modifiedUser;
}
