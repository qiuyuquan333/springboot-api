package com.qyq.springbootapi;

import com.qyq.springbootapi.aspect.Shop;
import com.qyq.springbootapi.util.encrypt.AesUtil;
import com.qyq.springbootapi.util.wechat.QRCodeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootApiApplicationTests {

    @Autowired
    private Shop shop;
    @Autowired
    private RestTemplate restTemplate;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void contextLoads(){
        Map<String,Object> map = new HashMap();
        map.put("name","张三");
        map.put("age",18);
        map.put("sex","女");

//        for (Object o : map.keySet()) {
//            System.out.println("key="+o+",value="+map.get(o));
//        }

        Iterator<Map.Entry<String,Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Object> next = iterator.next();
            System.out.println(next.getKey()+",value="+next.getValue());
        }

//        for (Entry<String,Object> entry:  map.entrySet()){
//            System.out.println(entry.getKey()+","+entry.getValue());
//        }

        for (Object o : map.values()){
            System.out.println(o);
        }

    }

    @Test
    public void Test2(){
        //输入流表示从一个源读取数据，输出流表示向一个目标写数据。

//        File file = new File("F://a.txt");
//        try {
//
//            FileWriter writer = new FileWriter(file);
//            writer.write("这是一段测试文本！");
//            writer.flush();
//            writer.close();
//
//            BufferedReader reader = new BufferedReader(new FileReader(file));
//            String line = reader.readLine();
//            System.out.println("读取："+line);
//        }catch (Exception e){
//            e.printStackTrace();
//        }

    }

    @Test
    public void Test3() throws Exception{
        String generateKey = AesUtil.getGenerateKeyString();
        System.out.println("密钥："+generateKey);
        String encrypt = AesUtil.encrypt("这是一段测试的文本", generateKey);
        System.out.println("加密后的数据："+encrypt);
        String decrypt = AesUtil.decrypt(encrypt, generateKey);
        System.out.println("解密后的内容："+decrypt);

    }

    /**
     * 根据内容，小图片生成二维码
     * @throws Exception
     */
    @Test
    public void Test4() throws Exception{
        String encode = URLEncoder.encode("http://crm.xyingyan.com.cn/test/wechat/authori", "UTF-8");
        // 存放在二维码中的内容
        String text = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx7a5ab729c84a0c99&redirect_uri="+encode+"&response_type=code&scope=snsapi_userinfo&state=s#wechat_redirect";
        // 嵌入二维码的图片路径
        String imgPath = "E:/image/Heat.jpg";
        // 生成的二维码的路径及名称
        String destPath = "E:/image/login.jpg";
        //生成二维码
        QRCodeUtil.encode(text, imgPath, destPath, true);
        // 解析二维码
        String str = QRCodeUtil.decode(destPath);
        // 打印出解析出的内容
        System.out.println(str);

    }

    @Test
    public void Test5(){
        Map<String,String> map = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });

        map.put("dv","dv");
        map.put("kd","kd");
        map.put("lbj","lbj");
        map.put("ad","ad");

        for (String s : map.keySet()) {
            System.out.println(s);
        }

    }

}
