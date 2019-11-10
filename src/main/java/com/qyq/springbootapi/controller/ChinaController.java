package com.qyq.springbootapi.controller;

import com.qyq.springbootapi.util.UnicodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 中国联保第三方接口测试
 */
@RestController
public class ChinaController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/china")
    public String Chaina(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("appId","69401C3B-A23E-A138-7CEA-FA47E5BCFEA3");
        headers.add("appSecret","49e6f4b9-1123-4570-b99b-1c0687csdfz");

        HttpEntity httpEntity = new HttpEntity(headers);
        Map<String,String> map = new HashMap<>();
        map.put("apiName","userTokenGet");
        map.put("format","json");
        map.put("time","1528264434");
        System.out.println("前面的map："+map);

        StringBuilder builder = new StringBuilder();
        builder.append("49e6f4b9-1123-4570-b99b-1c0687csdfz");
        TreeMap<String,String> treeMap = new TreeMap(map);
        Iterator<Map.Entry<String, String>> iterator = treeMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, String> entry = iterator.next();
           builder.append(entry.getKey()+entry.getValue()+"&");
        }
        builder.deleteCharAt(builder.length()-1);
        builder.append("49e6f4b9-1123-4570-b99b-1c0687csdfz");
        System.out.println("拼接字符串："+builder.toString());
        //MD5加密
        String digest = DigestUtils.md5DigestAsHex(builder.toString().getBytes()).toUpperCase();
        System.out.println("sign签名："+digest);

        map.put("sign",digest);
        System.out.println("后面的map："+map);

        ResponseEntity<String> exchange = restTemplate.exchange("http://interface.cug2313.com:8081/api?apiName={apiName}&format={format}&time={time}&sign={sign}",
                HttpMethod.POST, httpEntity, String.class, map);
        StringBuilder builder1 = new StringBuilder();
        builder1.append(exchange.getHeaders());
        builder1.append(exchange.getStatusCode());
        builder1.append(exchange.getStatusCodeValue());
        builder1.append(UnicodeUtil.decode(exchange.getBody()));
        return builder1.toString();

    }


}
