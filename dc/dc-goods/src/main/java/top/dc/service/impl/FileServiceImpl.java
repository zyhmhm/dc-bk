package top.dc.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import top.dc.service.FileService;
import top.dc.vo.ImageVo;
import top.dc.vo.SysResult;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@PropertySource("classpath:/properties/image.properties")
public class FileServiceImpl implements FileService {

    @Value("${image.localDirPath}")
    private String basePath;

    @Value("${image.urlDirPath}")
    private String urlDirPath;

    @Override
    public SysResult upLoadImages(MultipartFile image) {

        /*//1.准备基准目录(已经写入配置文件，自动生成)
        String basePath = "E:/SELF-DC/images/";
        File fileDir = new File(basePath);
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }*/

        if(image==null)
            return SysResult.fail("图片不能为空！");

        //2.判断上传的图片是否符合规则
        //2.1获取图片的名字
        String filename = image.getOriginalFilename();
        //2.2判断图片是不是要求的类型
        if(StringUtils.isEmpty(filename))
            return SysResult.fail("请传入合法文件！");
        filename = filename.toLowerCase();
        if(!filename.matches("^.+\\.(jpg|png)$")){
            return SysResult.fail("请传入jpg、png格式的图片");
        }
        try{
            //3.判断是否为恶意程序 转换为图片对象
            BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            if(width==0 || height==0){
                return SysResult.fail("传入的不是图片，请上传图片");
            }
            //4.说明图片验证成功，接下来进行存储操作，按照yyyy/MM/dd
            String dateDir = new SimpleDateFormat("yyyy/MM/dd/").format(new Date());
            String fileDirPath = basePath + dateDir;

            //验证路劲文件夹是否存在
            if(!new File(fileDirPath).exists())
                new File(fileDirPath).mkdirs();

            //5.生成文件名防止重名
                //获取文件后缀
            String fileType = filename.substring(filename.lastIndexOf("."));
            String uuid = UUID.randomUUID().toString();
            String localFileName = uuid + fileType;

            //6.将文件存储到本地
            String filePath = fileDirPath + localFileName;
            image.transferTo(new File(filePath));

            //获取网络虚拟路径
            String url = urlDirPath + dateDir + localFileName;
            ImageVo imageVo = new ImageVo().setHeight(height).setWidth(width).setUrl(url).setName(localFileName);
            return SysResult.success(imageVo);

        }catch (Exception e){
            e.printStackTrace();
            return SysResult.fail();
        }

    }
}
