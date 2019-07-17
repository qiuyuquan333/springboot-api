package com.qyq.springbootapi;

import com.qyq.springbootapi.util.AesUtil;
import com.qyq.springbootapi.util.RsaUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootApiApplicationTests {


    @Test
    public void contextLoads(){
        Map<String,Object> map = new HashMap();
        map.put("name","张三");
        map.put("age",18);
        map.put("sex","女");

//        for (Object o : map.keySet()) {
//            System.out.println("key="+o+",value="+map.get(o));
//        }

//        Iterator<Map.Entry<String,Object>> iterator = map.entrySet().iterator();
//        while (iterator.hasNext()){
//            Map.Entry<String, Object> next = iterator.next();
//            System.out.println(next.getKey()+",value="+next.getValue());
//        }

//        for (Entry<String,Object> entry:  map.entrySet()){
//            System.out.println(entry.getKey()+","+entry.getValue());
//        }

        for (Object o : map.values()){
            System.out.println(o);
        }

    }

    @Test
    public void Test2(){
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
        String generateKey = AesUtil.getGenerateKey();
        System.out.println("密钥："+generateKey);
        String encrypt = AesUtil.encrypt("这是一段测试的文本", generateKey);
        System.out.println("加密后的数据："+encrypt);
        String decrypt = AesUtil.decrypt(encrypt, generateKey);
        System.out.println("解密后的内容："+decrypt);

    }

    @Test
    public void Test4()throws Exception{
        String publicKey ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqNK7JiQ/o0Yc/MYRpHNsoH6ji/cVrAf+bCF1pztMDd5rplPLDGuOUR2SevmGuNi/gk0R0c7DggwSd56agpg6NcgPrWGI4dqazH02MCkDrtb58qwHTizGEC64jzxe7eyQ8dlCo59Xl6MD2mZ1sRworkM91eFnZR4w9qMDn8zKgnQIDAQAB";
        String generateKey = AesUtil.getGenerateKey();
        System.out.println("AES密钥："+generateKey);
        String des_key = RsaUtil.encrypt(generateKey, publicKey);
        System.out.println("加密过后的AES密钥："+des_key);
        String fuckYou = AesUtil.encrypt("fuck you", generateKey);
        System.out.println("加密的数据："+fuckYou);

    }


}
