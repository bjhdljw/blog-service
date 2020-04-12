package com.ljw.blogservice.util;

import com.ljw.blogservice.constant.ResponseCode;
import com.ljw.blogservice.exception.BlogServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
public class RSAUtil {

    public static final int MAX_DESCRYPT_BLOCK = 128;

    public static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * 生成密钥对
     * @return
     * @throws Exception
     */
    public static KeyPair getKeyPair() {
        KeyPair keyPair = null;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            log.info("生成密钥对失败");
            throw new BlogServiceException(ResponseCode.USER_GENERATE_KEY_ERROR.getCode(), ResponseCode.USER_GENERATE_KEY_ERROR.getMessage());
        }
        return keyPair;
    }

    /**
     * 把公钥字节数组转换为字符串
     * @param publicKey
     * @return
     */
    public static String getPublicKeyBase64(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    /**
     * 把私钥字节数组转换为字符串
     * @param privateKey
     * @return
     */
    public static String getPrivateKeyBase64(PrivateKey privateKey) {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    /**
     * 将私钥的base64编码，构造成一个PrivateKey实例
     * @param privateKeyBase64
     * @return
     * @throws Exception
     */
    public static PrivateKey generatorPrivateKeyObject(String privateKeyBase64) throws Exception{
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyBase64.getBytes()));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * 将公钥的base64编码构造成一个PublicKey实例
     * @param publicKeyBase64
     * @return
     * @throws Exception
     */
    public static PublicKey generatorPublicKeyObject(String publicKeyBase64) throws Exception{
        PublicKey publicKey = null;
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyBase64));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 使用私钥解密
     * @param targetBase64
     * @param privateKeyBase64
     * @return
     * @throws Exception
     */
    public static String descryptByPrivateKey(String targetBase64, String privateKeyBase64) throws Exception{
        byte[] target = Base64Utils.decodeFromString(targetBase64);
        PrivateKey privateKey = generatorPrivateKeyObject(privateKeyBase64);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] result = cipherBySegment(cipher, target, MAX_DESCRYPT_BLOCK);
        return Base64Utils.encodeToString(result);
    }

    /**
     * 使用私钥解密（不分段）
     * @return
     */
    public static String descrypt(String targetBase64, String privateKeyBase64) {
        String result = null;
        try {
            byte[] target = Base64Utils.decodeFromString(targetBase64);
            PrivateKey privateKey = generatorPrivateKeyObject(privateKeyBase64);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            result = new String(cipher.doFinal(target));
        } catch (Exception e) {
            log.info("密文：{}，私钥：{}", targetBase64, privateKeyBase64);
            throw new BlogServiceException(ResponseCode.USER_RSA_DESCRYPT_ERROR.getCode(), ResponseCode.USER_RSA_DESCRYPT_ERROR.getMessage());
        }
        return result;
    }

    /**
     * 使用公钥解密
     * @param target
     * @param base64PublicKey
     * @return
     * @throws Exception
     */
    public static String encrytByPublicKey(String target, String base64PublicKey) throws Exception{
        PublicKey publicKey = generatorPublicKeyObject(base64PublicKey);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] result = cipherBySegment(cipher, target.getBytes(), MAX_ENCRYPT_BLOCK);
        return Base64Utils.encodeToString(result);
    }

    /**
     * 分段加解密
     * @param cipher
     * @param target
     * @param segmentSize
     * @return
     * @throws Exception
     */
    public static byte[] cipherBySegment(Cipher cipher, byte[] target, int segmentSize) throws Exception{
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int inputLength = target.length;
        int offSet = 0;
        byte[] cache;
        int i = 0;
        while (inputLength - offSet > 0) {
            if(inputLength - offSet > segmentSize) {
                cache = cipher.doFinal(target, offSet, segmentSize);
            } else {
                cache = cipher.doFinal(target, offSet, inputLength - offSet);
            }
            byteArrayOutputStream.write(cache, 0, cache.length);
            i++;
            offSet = i * segmentSize;
        }
        byte[] data = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return data;
    }

}
