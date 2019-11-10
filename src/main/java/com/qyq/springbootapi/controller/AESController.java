package com.qyq.springbootapi.controller;

import com.alibaba.fastjson.JSON;
import com.qyq.springbootapi.result.ResponseResult;
import com.qyq.springbootapi.util.encrypt.AesUtil;
import com.qyq.springbootapi.util.encrypt.RsaUtil;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 使用RSA加密AES密钥，然后用AES加密数据进行传输
 * 非对称算法加解密速度慢，对称算法快。so....
 */
@RestController
public class AESController {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${RSA.publicKey}")
    private String publicKey;
    @Value("${RSA.privateKey}")
    private String privateKey;

    @GetMapping("/server")
    public ResponseResult TestApiController(@RequestParam("desKey") String desKey,
                                            @RequestParam("data") String data){
        try {
            String des_key = URLDecoder.decode(desKey, "UTF-8");
            String r_data = URLDecoder.decode(data, "UTF-8");
            System.out.println("接收到的加密AES密钥："+des_key);
            System.out.println("接收到的加密数据："+r_data);
            //先用RSA私钥解密得到AES密钥
            String decrypt = RsaUtil.decrypt(Base64.decodeBase64(des_key), privateKey ,false);
            System.out.println("解密后得到的AES密钥："+decrypt);
            //再使用AES密钥解密数据
            String s = AesUtil.decrypt(r_data, decrypt);
            System.out.println("AES密钥解密后的数据："+s);
            //加签返回信息
            String sign = RsaUtil.sign(s, privateKey);
            return new ResponseResult(0,"成功",sign);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(0,"失败",404);
        }


    }

    @GetMapping("/client")
    public ResponseResult TestApiController1(String content){
        try {
            //生成AES随机密钥
            String generateKey = AesUtil.getGenerateKeyString();
            System.out.println("生成AES随机密钥："+generateKey);
            //使用RSA加密AES密钥
            String desKey = RsaUtil.encrypt(generateKey.getBytes(), publicKey,true);
            System.out.println("公钥加密AES密钥："+desKey);
            //AES加密数据
            String data = AesUtil.encrypt(content, generateKey);
            URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/server").queryParam("desKey", URLEncoder.encode(desKey,"UTF-8"))
                    .queryParam("data",URLEncoder.encode(data,"UTF-8")).build().toUri();
            ResponseResult result = restTemplate.getForObject(uri, ResponseResult.class);
            String jsonString = JSON.toJSONString(result.getData());
            System.out.println("返回的签证信息："+jsonString);
            boolean verify = RsaUtil.verify(content, publicKey, jsonString);
            System.out.println("验签结果："+verify);
            return new ResponseResult(0,"成功",verify);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(0,"失败",404);
        }

    }



}
