package com.ljw.blogservice.service.user.Impl;

import com.ljw.blogservice.constant.RedisConstant;
import com.ljw.blogservice.domain.request.SetAESKey;
import com.ljw.blogservice.domain.user.UserInfo;
import com.ljw.blogservice.service.user.UserService;
import com.ljw.blogservice.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RedisTemplate redisTemplate;

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
        String privateKeyBase64 = (String) redisTemplate.opsForValue().get(RedisConstant.PRIVATEKEY + setAESKey.getUserName());
        String AESKey = RSAUtil.descryptByPrivateKey(setAESKey.getKey(), privateKeyBase64);
        redisTemplate.opsForValue().set(RedisConstant.AESKEY + setAESKey.getUserName(), AESKey, 10, TimeUnit.MINUTES);
    }

    @Override
    public void register(UserInfo userInfo) {
        String activeCode = UUID.randomUUID().toString().replaceAll("-", "");
        redisTemplate.opsForValue().set(RedisConstant.ACTIVECODE + activeCode, userInfo, 30, TimeUnit.MINUTES);

    }
}
