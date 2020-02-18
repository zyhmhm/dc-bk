package top.dc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import top.dc.service.FileService;
import top.dc.vo.SysResult;

@Controller
@RequestMapping("/file/")
@CrossOrigin
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 实现上传图片
     * @param file  上传的图片
     * @return
     */
    @PostMapping("pic/upload")
    @ResponseBody
    public SysResult picUpload(MultipartFile file){

        return fileService.upLoadImages(file);
    }
}
