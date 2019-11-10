package com.qyq.springbootapi.controller;

import com.alibaba.fastjson.JSON;
import com.qyq.springbootapi.result.BaiduResult;
import com.qyq.springbootapi.result.ResponseResult;
import com.qyq.springbootapi.util.encrypt.RsaUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;

@RestController
public class APIController {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${RSA.publicKey}")
    private String publicKey;
    @Value("${RSA.privateKey}")
    private String privateKey;

    /**
     * 百度地图第三方接口测试
     * @param query
     * @param region
     * @param output
     * @return
     */
    @RequestMapping("/baidu/{query}/{region}/{output}")
    public BaiduResult BaiduApiController(@PathVariable("query") String query,@PathVariable("region") String region,@PathVariable("output") String output){
        ResponseEntity<String> entity = restTemplate.getForEntity("http://api.map.baidu.com/place/v2/search?query="+query+"&region="+region+"&output="+output+"&ak=zlGSZV51xCwMUprdbFefGL41wrvU60Vb", String.class);
        String body = entity.getBody();
        BaiduResult baiduResult = JSON.parseObject(body, BaiduResult.class);
        return baiduResult;
    }


    /**
     * RSA
     * @param result
     * @return
     */

    @ApiOperation(value = "服务端第三方接口",notes = "接收加密过后的字符串进行解密并返回签证信息" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "result",value = "加密字符串",required = true,dataType = "String")
    })
    @GetMapping("/test")
    public ResponseResult TestApiController(@RequestParam("result") String result){

        try {
            String decode = URLDecoder.decode(result, "UTF-8");
            System.out.println("接收到的加密数据："+decode);
            //解密
            String decrypt = RsaUtil.decrypt(Base64.decodeBase64(decode), privateKey,false);
            System.out.println("解密后的内容："+decrypt);
            //加签返回信息
            String sign = RsaUtil.sign(decode, privateKey);
            return new ResponseResult(0,"成功",sign);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(0,"失败",404);
        }

    }

    @GetMapping("/get")
    public ResponseResult TestApiController1(String content){
        try {
            //公钥加密
            String encrypt = RsaUtil.encrypt(content.getBytes(), publicKey,true);
            System.out.println("公钥加密："+encrypt);
            URI uri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/test").queryParam("result", URLEncoder.encode(encrypt,"UTF-8")).build().toUri();
            ResponseResult object = restTemplate.getForObject(uri, ResponseResult.class);
            String data = JSON.toJSONString(object.getData());
            System.out.println("返回的签证信息："+data);
            boolean verify = RsaUtil.verify(encrypt, publicKey, data);
            System.out.println("验签结果："+verify);
            return new ResponseResult(0,"成功",verify);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(0,"失败",404);
        }

    }

}
