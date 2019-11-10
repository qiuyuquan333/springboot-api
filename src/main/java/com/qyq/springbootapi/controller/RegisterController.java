package com.qyq.springbootapi.controller;

import com.qyq.springbootapi.util.encrypt.AesUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 软件注册demo服务端
 */
@Controller
public class RegisterController {

    @Value("${AES.key}")
    private String key;

    @GetMapping("/")
    public String indexController(){
        return "index.html";
    }

    @GetMapping("/wechat")
    public String wechatController(){
        return "wechat.html";
    }

    /**
     * 根据客服提供的申请码，加上授权时间生成授权码
     * @param code
     * @param time
     * @return
     */
    @GetMapping("/register")
    @ResponseBody
    public String Register(String code,String time){
        try {
            String replace = code.replace(" ", "+");//转义，+号转空格
            String decrypt = AesUtil.decrypt(replace, key);
            String[] split = decrypt.split(",");
            String cpu = split[0];
            String application_time = split[1];
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parse = dateFormat.parse(application_time);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parse);
            calendar.add(Calendar.DAY_OF_YEAR,Integer.parseInt(time));
            Date date = calendar.getTime();
            String format = dateFormat.format(date);

            String data = cpu+","+format;
            System.out.println(data);
            String encrypt = AesUtil.encrypt(data, key);

            return encrypt;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
