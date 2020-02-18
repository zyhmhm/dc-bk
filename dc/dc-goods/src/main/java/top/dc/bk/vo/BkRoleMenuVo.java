package top.dc.bk.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BkRoleMenuVo implements Serializable {
    private static final long serialVersionUID = -315459860273139525L;

    /**角色id*/
    private Integer id;
    /**角色名称*/
    private String roleName;
    /**角色备注*/
    private String note;
    /**角色对应的菜单id*/
    private List<Integer> menuIds;

}
