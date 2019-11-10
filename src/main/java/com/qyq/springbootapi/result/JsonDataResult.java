package com.qyq.springbootapi.result;

/**
 * 接收json数据的结果集
 */
public class JsonDataResult {

    private String aes_key;

    private String data;

    public String getAes_key() {
        return aes_key;
    }

    public void setAes_key(String aes_key) {
        this.aes_key = aes_key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
