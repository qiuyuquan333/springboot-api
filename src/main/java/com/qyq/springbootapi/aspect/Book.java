package com.qyq.springbootapi.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class Book {

    @Pointcut("execution(* com.qyq.springbootapi.aspect.Shop.pay())")
    public void print(){

    }

    @Around("print()")
    public void aroud(ProceedingJoinPoint jp){
        try {
            System.out.println("环绕通知qian!");
            Object proceed = jp.proceed();
            System.out.println("proceed："+proceed);
            System.out.println("环绕通知hou!");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @AfterThrowing("print()")
    public void throwReturn(){
        System.out.println("异常！！！");
    }

    @AfterReturning("print()")
    public void afterReturn(){
        System.out.println("afterReturn方法！");
    }

    @After("print()")
    public void after(){
        System.out.println("后置通知！");
    }

    @Before("print()")
    public void before(){
        System.out.println("前置通知！");
    }
}
