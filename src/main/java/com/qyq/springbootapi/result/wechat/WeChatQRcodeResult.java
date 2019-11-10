package com.qyq.springbootapi.result.wechat;

/**
 * 微信获取二维码返回结果集
 */
public class WeChatQRcodeResult {
    private String ticket;

    private long expire_seconds;

    private String url;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public long getExpire_seconds() {
        return expire_seconds;
    }

    public void setExpire_seconds(long expire_seconds) {
        this.expire_seconds = expire_seconds;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
