package com.qyq.springbootapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
public class UserController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/coo")
    public String CooController(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie cookie = new Cookie("name","这是一个cookie");
       System.out.println("..."+cookie.getName()+"...."+cookie.getValue());
       cookie.setMaxAge(24*60*60);
       response.addCookie(cookie);
       return "/caa";
    }

    @RequestMapping("/caa")
    @ResponseBody
    public String CaaController(HttpServletRequest request,HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            System.out.println("name:"+cookie.getName()+",value:"+cookie.getValue()+",maxAge"+cookie.getMaxAge());
        }
        return request.getMethod();

    }

    @PostMapping("/fileUpload")
    @ResponseBody
    public void upload(@RequestParam(value = "file") MultipartFile file){
        if (file.isEmpty()) {
            System.out.println("文件为空空");
        }
        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String filePath = "E://WorkSpace"; // 上传后的路径
        fileName = UUID.randomUUID() + suffixName; // 新文件名
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @RequestMapping("/logger/{name}/{sex}")
    @ResponseBody
    public String loggerController(@PathVariable String name,@PathVariable String sex){
        logger.info(logger.getName());
        logger.info("进入方法：参数为 [{}]，[{}]",name,sex);
        name = "yxy";
        sex = "女";
        logger.info("重设后的值：[{}]，[{}]",name,sex);
        return sex+name;
    }

//    @ResponseBody
    @RequestMapping(value = "/servlet",method = RequestMethod.POST)
    public void servletTest(HttpServletRequest request,HttpServletResponse response){
        logger.info("请求URL：{}，请求方式：{}",request.getRequestURL(),request.getContentType());
        String name = request.getParameter("name");
        String age = request.getParameter("age");
        response.setContentType("application/json;charset=utf-8");
        try {
            response.getWriter().write(name+" : "+age);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @PostMapping("/jsonTest")
    public String jsonTest(@RequestBody Map map){
        logger.info("接收到的数据：param1：【{}】,param2：【{}】",map.get("productsList"),map.get("numberList"));
        return "true";
    }

}
