package top.dc.bk.service;

import top.dc.pojo.bk.pojo.BkLogs;
import top.dc.vo.SysResult;

public interface BkLogsService {
    //分页查询
    SysResult findGoodsByPage(Integer currentPage, Integer pageSize, String goodsName);
    //批量删除
    SysResult doDeleteLogsByIds(Integer[] ids);
    //添加
    void saveObject(BkLogs bkLog);

}
