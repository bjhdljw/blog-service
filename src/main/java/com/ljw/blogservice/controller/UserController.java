package com.ljw.blogservice.controller;

import com.ljw.blogservice.domain.request.PublicKeyGet;
import com.ljw.blogservice.domain.response.Result;
import com.ljw.blogservice.service.mail.MailService;
import com.ljw.blogservice.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
@RequestMapping("/userController")
public class UserController {

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping("/testMail")
    public void testMail() throws Exception{
        mailService.sendMail("bjcystl@163.com", "lll", "ttt");
    }

    @ResponseBody
    @RequestMapping(value = "/getPublicKey", method = RequestMethod.POST)
    public Result getPublicKey(@RequestBody @Valid PublicKeyGet publicKeyGet, BindingResult bindingResult) throws Exception {
        String publicKey = userService.createPublicKey(publicKeyGet.getUserName());
        return new Result().setData(publicKey);
    }

}
