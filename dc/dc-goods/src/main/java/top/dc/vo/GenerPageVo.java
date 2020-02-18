package top.dc.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import top.dc.pojo.BasePojo;

@Data
@Accessors(chain = true)
public class GenerPageVo extends BasePojo {
    private static final long serialVersionUID = -8950630472753276742L;
    private Integer id;
    private String generName;
    private String shopName;
}
