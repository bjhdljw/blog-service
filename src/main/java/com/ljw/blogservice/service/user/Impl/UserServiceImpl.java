package com.ljw.blogservice.service.user.Impl;

import com.ljw.blogservice.constant.RedisConstant;
import com.ljw.blogservice.constant.ResponseCode;
import com.ljw.blogservice.dao.UserDao;
import com.ljw.blogservice.domain.request.Login;
import com.ljw.blogservice.domain.request.SetAESKey;
import com.ljw.blogservice.domain.response.Session;
import com.ljw.blogservice.domain.user.UserInfo;
import com.ljw.blogservice.exception.BlogServiceException;
import com.ljw.blogservice.exception.ParameterValidException;
import com.ljw.blogservice.service.mail.MailService;
import com.ljw.blogservice.service.user.UserService;
import com.ljw.blogservice.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private HttpServletRequest request;

    @Override
    public String createPublicKey(String userName) throws Exception {
        //0.用户名唯一校验，mysql存在报错
        //1.产生密钥对
        //2.将私钥存储在缓存里，userName-privateKey
        //3.将公钥的Base64编码返回
        if(!CollectionUtils.isEmpty(userDao.getListByUserName(userName))) {
            log.info("用户名已存在：{}", userName);
            throw new BlogServiceException(ResponseCode.USER_NAME_EXIST_ERROR.getCode(), ResponseCode.USER_NAME_EXIST_ERROR.getMessage());
        }
        KeyPair keyPair = RSAUtil.getKeyPair();
        String privateKeyBase64 = RSAUtil.getPrivateKeyBase64(keyPair.getPrivate());
        redisTemplate.opsForValue().set(RedisConstant.PRIVATEKEY + userName, privateKeyBase64, 5, TimeUnit.MINUTES);
        String publicKeyBase64 = RSAUtil.getPublicKeyBase64(keyPair.getPublic());
        return publicKeyBase64;
    }

    @Override
    public void setKey(SetAESKey setAESKey) throws Exception{
        //0.用户名唯一校验
        //1.根据用户名，从redis中取出私钥
        //2.使用私钥将密文解密，得到了AES对称密钥
        //3.将对称密钥保存在缓存中
        if(!CollectionUtils.isEmpty(userDao.getListByUserName(setAESKey.getUserName()))) {
            log.info("用户名已存在：{}", setAESKey.getUserName());
            throw new BlogServiceException(ResponseCode.USER_NAME_EXIST_ERROR.getCode(), ResponseCode.USER_NAME_EXIST_ERROR.getMessage());
        }
        String privateKeyBase64 = (String) redisTemplate.opsForValue().get(RedisConstant.PRIVATEKEY + setAESKey.getUserName());
        String AESKey = RSAUtil.descrypt(setAESKey.getKey(), privateKeyBase64);
        redisTemplate.opsForValue().set(RedisConstant.AESKEY + setAESKey.getUserName(), AESKey, 30, TimeUnit.MINUTES);
    }

    @Override
    public void register(UserInfo userInfo) throws Exception{
        //0.用户名、邮箱唯一校验
        //1.生成激活码，将激活码和用户信息一同存储到redis里
        //2.向用户发送激活邮件
        if(!CollectionUtils.isEmpty(userDao.getListByUserName(userInfo.getUserName()))) {
            log.info("用户名已存在：{}", userInfo.getUserName());
            throw new BlogServiceException(ResponseCode.USER_NAME_EXIST_ERROR.getCode(), ResponseCode.USER_NAME_EXIST_ERROR.getMessage());
        }
        if(!CollectionUtils.isEmpty(userDao.getListByMail(userInfo.getMail()))) {
            log.info("邮箱已使用：{}", userInfo.getMail());
            throw new BlogServiceException(ResponseCode.USER_MAIL_USED_ERROR.getCode(), ResponseCode.USER_MAIL_USED_ERROR.getMessage());
        }
        String activeCode = UUID.randomUUID().toString().replaceAll("-", "");
        redisTemplate.opsForValue().set(RedisConstant.ACTIVECODE + activeCode, userInfo, 30, TimeUnit.MINUTES);
        mailService.sendActiveMail(userInfo, activeCode);
    }

    @Override
    public void active(String code) throws Exception{
        //1.验证激活码真实有效（去redis里面查看是否存在这个激活码）校验
        //2.根据这个激活码，从redis里取出用户信息
        //3.根据用户名取出redis中存放AES对称密钥，解密密码
        //4.使用MD5算法，计算密码的摘要
        //5.将用户信息和密码（不可逆）存储在mysql里面
        if(!redisTemplate.hasKey(RedisConstant.ACTIVECODE + code)) {
            throw new ParameterValidException(ResponseCode.USER_ACTIVE_CODE_ERROR.getCode(), ResponseCode.USER_ACTIVE_CODE_ERROR.getMessage());
        }
        Map map = (Map) redisTemplate.opsForValue().get(RedisConstant.ACTIVECODE + code);
        String AESKey = (String) redisTemplate.opsForValue().get(RedisConstant.AESKEY + map.get("userName"));
        String passWord = AESUtil.decrypt(map.get("password").toString(), AESKey);
        String md5OfPass = Md5Util.getMd5OfPass(passWord);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(map.get("userName").toString());
        userInfo.setPassword(md5OfPass);
        userInfo.setMail(map.get("mail").toString());
        addUser(userInfo);
    }

    @Override
    public void addUser(UserInfo userInfo) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        userInfo.setUuid(uuid);
        userDao.insertUser(userInfo);
    }

    @Override
    public Session login(Login login) throws Exception{
        //0.校验用户名存在于数据库中
        //1.从缓存中拿到对称密钥
        //2.使用对称密钥解密
        //3.计算密码的md5摘要，跟数据库中村的密码比对
        //4.生成token
        //5.token/用户信息存到缓存
        List<UserInfo> userInfos = userDao.getListByUserName(login.getUserName());
        if(CollectionUtils.isEmpty(userInfos)) {
            throw new BlogServiceException(ResponseCode.USER_LOGIN_USERNAME_ERROR.getCode(), ResponseCode.USER_LOGIN_USERNAME_ERROR.getMessage());
        }
        if(!redisTemplate.hasKey(RedisConstant.AESKEY + login.getUserName())) {
            throw new BlogServiceException(ResponseCode.USER_LOGIN_ERROR.getCode(), ResponseCode.USER_LOGIN_ERROR.getMessage());
        }
        String aesKey = (String) redisTemplate.opsForValue().get(RedisConstant.AESKEY + login.getUserName());
        String rawPassword = AESUtil.decrypt(login.getPassword(), aesKey);
        String md5OfPass = Md5Util.getMd5OfPass(rawPassword);
        UserInfo userInfo = userInfos.get(0);
        if(!userInfo.getPassword().equals(md5OfPass)) {
            throw new BlogServiceException(ResponseCode.USER_LOGIN_PASSWORD_ERROR.getCode(), ResponseCode.USER_LOGIN_PASSWORD_ERROR.getMessage());
        }
        String randomCode = UUID.randomUUID().toString().replace("-", "");
        Map<String, Object> map = new HashMap<>();
        map.put("randomCode", randomCode);
        map.put("userUuid", userInfo.getUuid());
        String token = JWTUtil.encode(map, RequestUtil.getIpAddr(request));
        Session session = new Session();
        session.setToken(token);
        redisTemplate.opsForValue().set(RedisConstant.TOKEN, randomCode, 30, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(RedisConstant.LOGIN + randomCode, userInfo, 30, TimeUnit.MINUTES);
        return session;
    }
}
