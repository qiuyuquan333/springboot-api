package com.qyq.springbootapi.exception;

public class TestException {

    public static void main(String[] args) {
        try {
            Test(2);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public static void Test(int i)throws Exception{
        if(i == 1){
            throw new MyException("换个大一点的数字？");
        }else {
            throw new MyException("这个数字还行！");
        }

    }
}
