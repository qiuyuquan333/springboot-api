package com.qyq.springbootapi.controller;

import com.alibaba.fastjson.JSON;
import com.qyq.springbootapi.mapper.UserInfoMapper;
import com.qyq.springbootapi.result.wechat.AccTokenResult;
import com.qyq.springbootapi.result.wechat.AccessToken;
import com.qyq.springbootapi.result.wechat.UserInfoResult;
import com.qyq.springbootapi.result.wechat.WeChatQRcodeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;

/**
 * 微信获取二维码，扫码登录
 */
@RequestMapping("/wechat")
@RestController
public class WeChatController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserInfoMapper userInfoMapper;

    String APPID = "wx7a5ab729c84a0c99";
    String APPSECRET = "323a86069b930185eafe607eb078a821";

    @RequestMapping("/getQRcode")
    public byte[] getQRcode(){
        AccTokenResult tokenResult = restTemplate.getForObject("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + APPID + "&secret=" + APPSECRET, AccTokenResult.class);

        String token = tokenResult.getAccess_token();

        String json = "{\n" +
                "\t\"expire_seconds\": 604800,\n" +
                "\t\"action_name\": \"QR_SCENE\",\n" +
                "\t\"action_info\": {\n" +
                "\t\t\"scene\": {\n" +
                "\t\t\t\"scene_id\": \"关注公众号\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}";
        WeChatQRcodeResult qRcodeResult = restTemplate.postForObject("https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + token, json, WeChatQRcodeResult.class);
        String ticket = qRcodeResult.getTicket();
        System.out.println("ticket："+ticket);
        try {

            byte[] object = restTemplate.getForObject("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + URLEncoder.encode(ticket, "UTF-8"), byte[].class);

//            byte2image(object,"E:\\image/code.jpg");

            return Base64.getEncoder().encode(object);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }


    }


    @RequestMapping("/authori")
    public String authoriController(HttpServletRequest request){
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        String object = restTemplate.getForObject("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + APPID + "&secret=" + APPSECRET + "&code=" + code + "&grant_type=authorization_code", String.class);
        AccessToken accessToken = JSON.parseObject(object, AccessToken.class);
        String access_token = accessToken.getAccess_token();
        String openid = accessToken.getOpenid();
        String scope = accessToken.getScope();

        String object1 = restTemplate.getForObject("https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid + "&lang=zh_CN", String.class);
        UserInfoResult infoResult = JSON.parseObject(object1, UserInfoResult.class);

        userInfoMapper.insertUserInfo(infoResult);

        return "欢迎你【"+infoResult.getNickname()+"】,扫码登录成功！";

    }


    //byte数组到图片
    public void byte2image(byte[] data,String path){
        if(data.length<3||path.equals("")) return;
        try{
            FileImageOutputStream imageOutput = new FileImageOutputStream(new File(path));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            System.out.println("Make Picture success,Please find image in " + path);
        } catch(Exception ex) {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();
        }
    }


}
