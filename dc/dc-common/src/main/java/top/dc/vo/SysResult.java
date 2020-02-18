package top.dc.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SysResult {

    private Integer status = 200;
    private String msg;
    private Object data;

    public static SysResult fail(){
        return new SysResult().setStatus(201).setMsg("执行失败！");
    }
    public static  SysResult fail(Integer status,String msg){
        return new SysResult().setStatus(status).setMsg(msg);
    }
    public static SysResult fail(String msg){
        return new SysResult().setStatus(201).setMsg(msg);
    }

    public static SysResult success(String msg,Object data){
        return new SysResult().setMsg(msg).setData(data);
    }
    public static SysResult success(Object data){
        return new SysResult().setData(data);
    }
    public static SysResult success(){
        return new SysResult();
    }
}
