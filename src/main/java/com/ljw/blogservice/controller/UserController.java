package com.ljw.blogservice.controller;

import com.ljw.blogservice.constant.ResponseCode;
import com.ljw.blogservice.domain.request.Login;
import com.ljw.blogservice.domain.request.PublicKeyGet;
import com.ljw.blogservice.domain.request.SetAESKey;
import com.ljw.blogservice.domain.response.Result;
import com.ljw.blogservice.domain.response.Session;
import com.ljw.blogservice.domain.user.UserInfo;
import com.ljw.blogservice.exception.ParameterValidException;
import com.ljw.blogservice.service.mail.MailService;
import com.ljw.blogservice.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/userController")
public class UserController {

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    /**
     * 获取公钥（注册、登陆）
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
     * 保存AES密钥（注册、登陆）
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

    /**
     * 缓存用户信息（注册）
     * @param userInfo
     * @param bindingResult
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Result register(@RequestBody @Valid UserInfo userInfo, BindingResult bindingResult) throws Exception{
        userService.register(userInfo);
        return new Result();
    }

    /**
     * 激活账户接口（注册）
     * @param httpServletRequest
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public Result active(HttpServletRequest httpServletRequest) throws Exception{
        String code = httpServletRequest.getParameter("activeCode");
        if(StringUtils.isEmpty(code)) {
            throw new ParameterValidException(ResponseCode.USER_ACTIVE_ERROR.getCode(), ResponseCode.USER_ACTIVE_ERROR.getMessage());
        }
        userService.active(code);
        return new Result();
    }

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody @Valid Login login, BindingResult bindingResult) throws Exception{
        Session session = userService.login(login);
        return new Result().setData(session);
    }


}
