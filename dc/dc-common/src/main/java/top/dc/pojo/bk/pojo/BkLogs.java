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
@TableName(value = "bk_logs")
public class BkLogs extends BasePojo {
    private static final long serialVersionUID = -5645973058288410003L;
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String userName;
    private String operation;
    private String method;
    private String params;
    private Long time;
    private String ip;
}
