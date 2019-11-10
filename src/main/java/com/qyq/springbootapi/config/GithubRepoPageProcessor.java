//package com.qyq.springbootapi.config;
//
//import us.codecraft.webmagic.Page;
//import us.codecraft.webmagic.Site;
//import us.codecraft.webmagic.Spider;
//import us.codecraft.webmagic.processor.PageProcessor;
//
//public class GithubRepoPageProcessor implements PageProcessor {
//
//    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10000).setCharset("gbk");
//
//
//    @Override
//    public void process(Page page) {
//        page.putField("provide",page.getHtml().xpath("//tr[@class=\"provincetr\"]/td/a/text()").all());
//        page.addTargetRequests(page.getHtml().links().regex("\\d+\\.html").all());
//    }
//
//    @Override
//    public Site getSite() {
//        return site;
//    }
//
//    public static void main(String[] args) {
//
//        Spider.create(new GithubRepoPageProcessor())
//                //从"https://github.com/code4craft"开始抓
//                .addUrl("http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2018/")
//                //开启5个线程抓取
//                .thread(5)
//                //启动爬虫
//                .run();
//    }
//}
