package com.ljw.blogservice.util;

import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {

    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";

    public static String encrypt(String target, String pass) throws Exception{
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(pass.getBytes(), "AES"));
        byte[] result = cipher.doFinal(target.getBytes("utf-8"));
        return Base64Utils.encodeToString(result);
    }

    public static String decrypt(String target, String decryptKey) throws Exception{
        byte[] targetBytes = Base64Utils.decodeFromString(target);
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
        byte[] result = cipher.doFinal(targetBytes);
        return new String(result);
    }

}
