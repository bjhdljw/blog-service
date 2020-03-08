package com.ljw.blogservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Calendar;

@Slf4j
@Controller
@RequestMapping("/ueditor")
public class FileController {

    @RequestMapping(value = "/file")
    @ResponseBody
    public String file(MultipartFile file, HttpServletRequest httpServletRequest) {
        //拿到action参数，并判断
        String action = httpServletRequest.getParameter("action");
        if(action.equals("uploadimage")) {
            log.info("准备上传图片");
            return uploadImage(file);
        }
        log.info("请求已经打到后台了！！！");
        String string = "{\n" +
                "                \"imageActionName\": \"uploadimage\",\n" +
                "                \"imageFieldName\": \"file\", \n" +
                "                \"imageMaxSize\": 2048000, \n" +
                "                \"imageAllowFiles\": [\".png\", \".jpg\", \".jpeg\", \".gif\", \".bmp\"], \n" +
                "                \"imageCompressEnable\": true, \n" +
                "                \"imageCompressBorder\": 1600, \n" +
                "                \"imageInsertAlign\": \"none\", \n" +
                "                \"imageUrlPrefix\": \"http://localhost:8083/api/ueditor/images/\",\n" +
                "                \"imagePathFormat\": \"/ueditor/jsp/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}\" }";
        return string;
    }

    public String uploadImage(MultipartFile file){
        if(file.isEmpty()) {
            return "error";
        }
        //TODO 把图片存储到本地 文件服务器、磁盘挂载、云存储
        //第一步：生成一个唯一的图片的名字、文件格式的校验（文件后缀）
        String originalName = file.getOriginalFilename();
        String suffixName = originalName.substring(originalName.lastIndexOf("."));
        String fileName = Calendar.getInstance().getTimeInMillis() + suffixName;
        //第二部：存储文件
        String path = "D:\\images\\" + fileName;
        File destination = new File(path);
        if(!destination.getParentFile().exists()) {
            destination.getParentFile().mkdir();
        }
        try {
            file.transferTo(destination);
            return "{\"state\": \"SUCCESS\"," +
                    "\"url\": \"" + "" + fileName + "\"," +
                    "\"title\": \"" + fileName + "\"," +
                    "\"original\": \"" + fileName + "\"}";
        } catch (Exception e) {
            log.info("文件存储异常");
        }
        return "error";
    }

    @RequestMapping("images/{fileName}")
    public void getImage(@PathVariable("fileName") String fileName, HttpServletResponse httpServletResponse) throws Exception{

        if(StringUtils.isEmpty(fileName)) {
            fileName = "";
        }
        //去本地拿到这个图片
        String path = "D:\\images\\" + fileName;
        File file = new File(path);
        if(!file.exists() || !file.canRead()) {
            throw new Exception("获取文件异常");
        }
        FileInputStream inputStream = new FileInputStream(file);
        byte[] data = new byte[(int)file.length()];
        inputStream.read(data);
        inputStream.close();
        OutputStream outputStream = httpServletResponse.getOutputStream();
        httpServletResponse.setContentType("image/png");
        outputStream.write(data);
        outputStream.flush();
        outputStream.close();
    }

}
