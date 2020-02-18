package top.dc.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
public class BasePojo implements Serializable {
    private static final long serialVersionUID = 7219109033389945509L;

    private Date createTime;
    private Date updateTime;

}
