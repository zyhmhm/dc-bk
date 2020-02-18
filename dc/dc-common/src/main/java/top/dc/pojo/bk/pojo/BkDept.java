package top.dc.pojo.bk.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import top.dc.pojo.BasePojo;

@Data
@Accessors(chain = true)
@TableName("bk_depts")
public class BkDept extends BasePojo {
    private static final long serialVersionUID = 8876920804134951849L;
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String deptName;
    private Integer parentId;
    private Integer sort;
    private String note;
    private String createUser;
    private String modifiedUser;
}
