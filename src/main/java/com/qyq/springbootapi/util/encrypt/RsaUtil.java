package com.qyq.springbootapi.util.encrypt;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA生成密钥，加解密工具类
 */
public class RsaUtil {

    /**
     * 指定加密算法
     */
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * 指定生成多少位的密钥
     */
    private static final int KEY_BIT = 1024;

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     * 1024位的密钥要改成128,2048位的才是256
     */
    private static final int MAX_DECRYPT_BLOCK = 256;

    /**
     * 签名算法
     */
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";


    /**
     * 生成密钥对
     * @return
     * @throws Exception
     */
    public static Map<String,Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGenerator.initialize(KEY_BIT);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();//生成公钥
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();//生成密钥
        Map<String,Object> map = new HashMap<>(2);
        map.put("publicKey",rsaPublicKey);
        map.put("privateKey",rsaPrivateKey);
        return map;
    }

    /**
     * 获取公钥
     * @param map
     * @return
     */
    public static String getPublicKey(Map<String,Object> map){
        Key key = (Key) map.get("publicKey");
        String publicKey = Base64.encodeBase64String(key.getEncoded());
        return publicKey;
    }

    /**
     * 获取私钥
     * @param map
     * @return
     */
    public static String getPrivateKey(Map<String,Object> map){
        Key key = (Key) map.get("privateKey");
        String privateKey = Base64.encodeBase64String(key.getEncoded());
        return privateKey;
    }

    /**
     * 公钥、私钥分段加密 --》公钥、私钥加密
     * @param data
     * @param key
     * @param isPublic 是否为公钥
     * @return
     * @throws Exception
     */
    public static String encrypt(byte[] data, String key, boolean isPublic) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        if(isPublic){
            PublicKey publicK = StringToPublicKey(key);
            cipher.init(Cipher.ENCRYPT_MODE, publicK);
        }else {
            PrivateKey privateK = StringToPrivateKey(key);
            cipher.init(Cipher.ENCRYPT_MODE, privateK);
        }
        int inputLen = data.length;
        return Segment(inputLen,cipher,data,MAX_ENCRYPT_BLOCK,true);
    }

    /**
     * 公钥、私钥分段解密 --》公钥、私钥解密
     * @param data
     * @param key
     * @param isPublic 是否为公钥
     * @return
     * @throws Exception
     */
    public static String decrypt(byte[] data, String key, boolean isPublic) throws Exception {
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        if(isPublic){
            PublicKey publicK = StringToPublicKey(key);
            cipher.init(Cipher.DECRYPT_MODE, publicK);
        }else{
            PrivateKey privateK = StringToPrivateKey(key);
            cipher.init(Cipher.DECRYPT_MODE, privateK);
        }
        int inputLen = data.length;
        return Segment(inputLen,cipher,data,MAX_DECRYPT_BLOCK,false);
    }

    /**
     * 加签
     * @param data 数据
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String sign(String data,String privateKey)throws Exception{
        PrivateKey privateK = StringToPrivateKey(privateKey);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data.getBytes());
        return Base64.encodeBase64String(signature.sign());
    }

    /**
     * 验证数字签名
     * @param data 数据
     * @param publicKey 公钥
     * @param sign 签名
     * @return
     * @throws Exception
     */
    public static boolean verify(String data,String publicKey,String sign)throws Exception{
        PublicKey publicK = StringToPublicKey(publicKey);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data.getBytes());
        return signature.verify(Base64.decodeBase64(sign));
    }

    /**
     * String类型的公钥转换为PublicKey类型
     * @param publicKey
     * @return
     * @throws Exception
     */
    private static PublicKey StringToPublicKey(String publicKey)throws Exception{
        byte[] bytes = Base64.decodeBase64(publicKey);//先把公钥进行解码
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(x509EncodedKeySpec);
        return publicK;
    }

    /**
     * String类型的私钥转换为PrivateKey类型
     * @param privateKey
     * @return
     * @throws Exception
     */
    private static PrivateKey StringToPrivateKey(String privateKey)throws Exception{
        byte[] bytes = Base64.decodeBase64(privateKey);//先对私钥进行解码
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(bytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        return privateK;
    }

    /**
     * 分段加、解密
     * @param inputLen 字节长度
     * @param cipher 加密实例类
     * @param data 数据
     * @param MAX_BLOCK 指定加解密最大明文大小
     * @param isEncrypt 是否为加密
     * @return
     * @throws Exception
     */
    private static String Segment(Integer inputLen,Cipher cipher,byte[] data,Integer MAX_BLOCK, boolean isEncrypt)throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_BLOCK) {
                cache = cipher.doFinal(data, offset, MAX_BLOCK);
            } else {
                cache = cipher.doFinal(data, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        if(isEncrypt){
            //如果为加密，进行编码
            return Base64.encodeBase64String(encryptedData);
        }else {
            //解密则为解码
            return new String(encryptedData, StandardCharsets.UTF_8);
        }
    }

}
