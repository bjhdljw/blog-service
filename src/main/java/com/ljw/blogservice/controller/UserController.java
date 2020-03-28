package com.ljw.blogservice.controller;

import com.ljw.blogservice.domain.request.PublicKeyGet;
import com.ljw.blogservice.domain.request.SetAESKey;
import com.ljw.blogservice.domain.response.Result;
import com.ljw.blogservice.domain.user.UserInfo;
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

//    @ResponseBody
//    @RequestMapping("/testMail")
//    public void testMail() throws Exception{
//        mailService.sendMail("bjcystl@163.com", "lll", "ttt");
//    }

    /**
     * 获取公钥
     * @param publicKeyGet
     * @param bindingResult
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/getPublicKey", method = RequestMethod.POST)
    public Result getPublicKey(@RequestBody @Valid PublicKeyGet publicKeyGet, BindingResult bindingResult) throws Exception {
        String publicKey = userService.createPublicKey(publicKeyGet.getUserName());
        return new Result().setData(publicKey);
    }

    /**
     * 保存AES密钥
     * @param setAESKey
     * @param bindingResult
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/setKey", method = RequestMethod.POST)
    public Result setKey(@RequestBody @Valid SetAESKey setAESKey, BindingResult bindingResult) throws Exception{
        userService.setKey(setAESKey);
        return new Result();
    }

    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Result register(@RequestBody @Valid UserInfo userInfo, BindingResult bindingResult) {
        return new Result();
    }


}
