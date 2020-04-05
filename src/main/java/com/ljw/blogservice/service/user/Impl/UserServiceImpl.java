package com.ljw.blogservice.service.user.Impl;

import com.ljw.blogservice.constant.RedisConstant;
import com.ljw.blogservice.dao.UserDao;
import com.ljw.blogservice.domain.request.SetAESKey;
import com.ljw.blogservice.domain.user.UserInfo;
import com.ljw.blogservice.service.mail.MailService;
import com.ljw.blogservice.service.user.UserService;
import com.ljw.blogservice.util.AESUtil;
import com.ljw.blogservice.util.Md5Util;
import com.ljw.blogservice.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MailService mailService;

    @Autowired
    private UserDao userDao;

    @Override
    public String createPublicKey(String userName) throws Exception {
        //1.产生密钥对
        //2.将私钥存储在缓存里，userName-privateKey
        //3.将公钥的Base64编码返回
        KeyPair keyPair = RSAUtil.getKeyPair();
        String privateKeyBase64 = RSAUtil.getPrivateKeyBase64(keyPair.getPrivate());
        redisTemplate.opsForValue().set(RedisConstant.PRIVATEKEY + userName, privateKeyBase64, 10, TimeUnit.MINUTES);
        String publicKeyBase64 = RSAUtil.getPublicKeyBase64(keyPair.getPublic());
        return publicKeyBase64;
    }

    @Override
    public void setKey(SetAESKey setAESKey) throws Exception{
        //1.根据用户名，从redis中取出私钥
        //2.使用私钥将密文解密，得到了AES对称密钥
        //3.将对称密钥保存在缓存中
        String privateKeyBase64 = (String) redisTemplate.opsForValue().get(RedisConstant.PRIVATEKEY + setAESKey.getUserName());
        String AESKey = RSAUtil.descryptByPrivateKey(setAESKey.getKey(), privateKeyBase64);
        redisTemplate.opsForValue().set(RedisConstant.AESKEY + setAESKey.getUserName(), AESKey, 10, TimeUnit.MINUTES);
    }

    @Override
    public void register(UserInfo userInfo) throws Exception{
        //1.生成激活码，将激活码和用户信息一同存储到redis里
        //2.向用户发送激活邮件
        String activeCode = UUID.randomUUID().toString().replaceAll("-", "");
        redisTemplate.opsForValue().set(RedisConstant.ACTIVECODE + activeCode, userInfo, 30, TimeUnit.MINUTES);
        mailService.sendActiveMail(userInfo, activeCode);
    }

    @Override
    public void active(String code) throws Exception{
        //1.验证激活码真实有效（去redis里面查看是否存在这个激活码）
        //2.根据这个激活码，从redis里取出用户信息
        //3.根据用户名取出redis中存放AES对称密钥，解密密码
        //4.使用MD5算法，计算密码的摘要
        //5.将用户信息和密码（不可逆）存储在mysql里面
        if(!redisTemplate.hasKey(RedisConstant.ACTIVECODE + code)) {
            return;
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
}
