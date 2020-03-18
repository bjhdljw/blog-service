package com.ljw.blogservice.controller;

import com.ljw.blogservice.service.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/userController")
public class UserController {

    @Autowired
    private MailService mailService;

    @ResponseBody
    @RequestMapping("/testMail")
    public void testMail() throws Exception{
        mailService.sendMail("bjcystl@163.com", "今天风很大", "测试邮件");
    }

}
