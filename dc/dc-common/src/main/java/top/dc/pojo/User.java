package top.dc.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("t_user")
public class User extends BasePojo{
    private static final long serialVersionUID = -6615037995338055694L;

    //用户id
    @TableId(type = IdType.AUTO)
    private Integer id;
    //用户名
    private String userName;
    //用户密码
    private String pwd;
    //电子邮箱
    private String emial;
    //电话
    private String tell;
    //地址
    private String addr;
    //用户标记
    private Integer mark;
}
