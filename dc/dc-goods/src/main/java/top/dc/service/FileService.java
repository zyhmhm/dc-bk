package top.dc.service;

import org.springframework.web.multipart.MultipartFile;
import top.dc.vo.SysResult;

public interface FileService {
    SysResult upLoadImages(MultipartFile images);
}
