package com.qyq.springbootapi.util.encrypt;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * AES生成密钥，加解密工具类
 */
public class AesUtil {

    /**
     * 指定加密算法
     */
    private static final String KEY_ALGORITHM = "AES";

    private static final String KEY_CIPHER = "AES/ECB/PKCS5Padding";

    /**
     * 指定生成多少位的密钥
     */
    private static final int KEY_BIT = 128;

    /**
     * 随机生成AES密钥-String类型
     * @return
     * @throws Exception
     */
    public static String getGenerateKeyString()throws Exception{
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(KEY_BIT);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] encoded = secretKey.getEncoded();
        return Base64.encodeBase64String(encoded);
    }

    /**
     * 随机生成AES密钥-Byte类型
     * @return
     * @throws Exception
     */
    public static byte[] getGenerateKeyByte()throws Exception{
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
        keyGenerator.init(KEY_BIT);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();

    }

    /**
     * 将String类型的密钥转化为SecretKey类型
     * @param generateKey
     * @return
     */
    private static SecretKey StringToSecretKey(String generateKey){
        byte[] bytes = Base64.decodeBase64(generateKey);
        SecretKeySpec spec = new SecretKeySpec(bytes,KEY_ALGORITHM);
        return spec;
    }

    /**
     * 将byte[]类型的密钥转化为SecretKey类型
     * @param generateKey
     * @return
     */
    private static SecretKey ByteToSecretKey(byte[] generateKey){
        SecretKeySpec spec = new SecretKeySpec(generateKey,KEY_ALGORITHM);
        return  spec;
    }

    /**
     * 加密
     * @param data
     * @param generateKey String类型
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String generateKey)throws Exception{
        SecretKey secretKey = StringToSecretKey(generateKey);
        byte[] bytes = data.getBytes();
        return enOrdeCrypt(secretKey,bytes,Cipher.ENCRYPT_MODE);
    }

    /**
     * 加密
     * @param data
     * @param generateKey byte[]类型
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, byte[] generateKey)throws Exception{
        SecretKey secretKey = ByteToSecretKey(generateKey);
        byte[] bytes = data.getBytes();
        return enOrdeCrypt(secretKey,bytes,Cipher.ENCRYPT_MODE);
    }

    /**
     * 解密
     * @param data
     * @param generateKey string类型
     * @return
     * @throws Exception
     */
    public static String decrypt(String data, String generateKey)throws Exception{
        SecretKey secretKey = StringToSecretKey(generateKey);
        byte[] bytes = Base64.decodeBase64(data);
        return enOrdeCrypt(secretKey,bytes,Cipher.DECRYPT_MODE);
    }

    /**
     * 解密
     * @param data
     * @param generateKey byte[]类型
     * @return
     * @throws Exception
     */
    public static String decrypt(String data, byte[] generateKey)throws Exception{
        SecretKey secretKey = ByteToSecretKey(generateKey);
        byte[] bytes = Base64.decodeBase64(data);
        return enOrdeCrypt(secretKey,bytes,Cipher.DECRYPT_MODE);
    }

    /**
     * 加、解密实现
     * @param secretKey
     * @param data
     * @param CipherType 指定加、解密操作
     * @return
     * @throws Exception
     */
    private static String enOrdeCrypt(SecretKey secretKey,byte[] data,int CipherType)throws Exception{
        Cipher cipher = Cipher.getInstance(KEY_CIPHER);
        cipher.init(CipherType,secretKey);
        byte[] aFinal = cipher.doFinal(data);
        if(CipherType == 1){
            return Base64.encodeBase64String(aFinal);//加密进行编码
        }else {
            return new String(aFinal, StandardCharsets.UTF_8);//解密进行解码
        }
    }

}
