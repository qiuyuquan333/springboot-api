package com.qyq.springbootapi.aspect;

import org.springframework.stereotype.Component;

@Component
public class Shop {

    public void pay(){
        System.out.println("这是pay方法！");
    }
}
