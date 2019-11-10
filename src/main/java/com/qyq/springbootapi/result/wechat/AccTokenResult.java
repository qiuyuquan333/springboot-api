package com.qyq.springbootapi.result.wechat;

/**
 * 获取access_token结果集
 */
public class AccTokenResult {
    private String access_token;

    private Integer expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }
}
