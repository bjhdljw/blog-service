package com.ljw.blogservice.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;

import java.security.MessageDigest;

@Slf4j
public class Md5Util {

    public static String getMd5OfPass(String pass) {
        String result = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(pass.getBytes());
            result = Base64Utils.encodeToString(messageDigest.digest());
            result = result.substring(0, result.length() - 8);
        } catch (Exception e) {
            log.info("获取MD5值失败：{}", e.getMessage());
        }
        return result;
    }

}
