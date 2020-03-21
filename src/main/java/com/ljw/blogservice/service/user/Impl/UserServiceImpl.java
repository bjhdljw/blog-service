package com.ljw.blogservice.service.user.Impl;

import com.ljw.blogservice.service.user.UserService;
import com.ljw.blogservice.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
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
        redisTemplate.opsForValue().set(userName, privateKeyBase64, 10, TimeUnit.MINUTES);
        String publicKeyBase64 = RSAUtil.getPublicKeyBase64(keyPair.getPublic());
        return publicKeyBase64;
    }
}
