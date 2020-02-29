package com.ljw.blogservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("/ueditor")
public class FileController {

    @RequestMapping("/file")
    @ResponseBody
    public String file() {
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

}
