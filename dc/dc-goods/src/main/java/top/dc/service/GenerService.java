package top.dc.service;

import top.dc.pojo.Gener;
import top.dc.vo.SysResult;

public interface GenerService {
    SysResult findGenerByPage(Integer currentPage);

    SysResult updateGenerNameById(Gener gener);

    //SysResult deleteGenerById(Integer id);
}
