package com.suiyia.callback;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CallbackApplicationTests {

    public static String getOutTradeNo(int parkID,int billType) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date date = new Date();
        String key = format.format(date);
        key+="1"+billType;
        String parkString=parkID+"";
        if(parkString!=null&&!parkString.isEmpty()){
            int length=parkString.length();
            if(length<7){
                int dataLength=7-length;
                for(int i=0;i<dataLength;i++){
                    parkString="0"+parkString;
                }
            }
        }else {
            parkString="0000000";
        }
        key +=parkString;
        java.util.Random r = new java.util.Random();
        key += String.format("%02d", Math.abs(r.nextInt()));
        key = key.substring(0, 28);
        return key;
    }

    @Test
    public void contextLoads(){
        for (int i = 0; i < 1000; i++) {
            String calid = getOutTradeNo(1332,1999);
            String url = "http://localhost/callback/request?calid="+calid;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name",getOutTradeNo(1332,1999));
            jsonObject.put("value",getOutTradeNo(1332,1999));
            jsonObject.put("year",getOutTradeNo(1332,1999));
            jsonObject.put("month",getOutTradeNo(1332,1999));
            jsonObject.put("day",getOutTradeNo(1332,1999));
            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url.toString()); // 执行 POST 请求，GET 请求有 HttpGet
            try {
                StringEntity httpEntity = new StringEntity(jsonObject.toString());
                post.setEntity(httpEntity);
                CloseableHttpResponse response = (CloseableHttpResponse) client.execute(post); // 得到返回结果
                System.out.println(EntityUtils.toString(response.getEntity()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}

